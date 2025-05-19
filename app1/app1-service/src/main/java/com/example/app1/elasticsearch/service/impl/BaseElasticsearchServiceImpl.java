package com.example.app1.elasticsearch.service.impl;

import com.example.app1.elasticsearch.service.ElasticsearchService;
import com.example.app1.elasticsearch.util.ElasticsearchQueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Elasticsearch服务基础实现类
 * @param <T> 文档类型
 */
@Slf4j
public abstract class BaseElasticsearchServiceImpl<T> implements ElasticsearchService<T> {

    protected final ElasticsearchOperations elasticsearchOperations;
    protected final Class<T> documentClass;

    @SuppressWarnings("unchecked")
    public BaseElasticsearchServiceImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.documentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void createIndex() {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(documentClass);
        if (!indexOperations.exists()) {
            indexOperations.create();
            indexOperations.putMapping(indexOperations.createMapping());
            log.info("索引 [{}] 创建成功", getIndexName());
        }
    }

    @Override
    public void deleteIndex() {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(documentClass);
        if (indexOperations.exists()) {
            indexOperations.delete();
            log.info("索引 [{}] 删除成功", getIndexName());
        }
    }

    @Override
    public boolean indexExists() {
        return elasticsearchOperations.indexOps(documentClass).exists();
    }

    @Override
    public T save(T document) {
        Assert.notNull(document, "Document must not be null");
        return elasticsearchOperations.save(document);
    }

    @Override
    public Iterable<T> saveAll(List<T> documents) {
        Assert.notNull(documents, "Documents must not be null");
        return elasticsearchOperations.save(documents);
    }

    @Override
    public Optional<T> findById(String id) {
        Assert.notNull(id, "Id must not be null");
        return Optional.ofNullable(elasticsearchOperations.get(id, documentClass));
    }

    @Override
    public void deleteById(String id) {
        Assert.notNull(id, "Id must not be null");
        elasticsearchOperations.delete(id, documentClass);
    }

    @Override
    public void deleteAll() {
        // 使用删除索引再创建的方式比逐个删除文档效率更高
        deleteIndex();
        createIndex();
    }

    @Override
    public List<T> findByField(String fieldName, Object value) {
        Assert.notNull(fieldName, "Field name must not be null");
        Assert.notNull(value, "Value must not be null");

        // 使用ElasticsearchQueryBuilder构建精确匹配查询
        Query query = ElasticsearchQueryBuilder.buildTermQuery(fieldName, value);

        SearchHits<T> searchHits = elasticsearchOperations.search(query, documentClass);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findByFields(Map<String, Object> fieldValues) {
        Assert.notNull(fieldValues, "Field values must not be null");
        Assert.notEmpty(fieldValues, "Field values must not be empty");

        // 使用ElasticsearchQueryBuilder构建多字段查询
        Query query = ElasticsearchQueryBuilder.buildMultiFieldCriteriaQuery(fieldValues);

        SearchHits<T> searchHits = elasticsearchOperations.search(query, documentClass);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> searchByKeyword(String keyword, String... fieldNames) {
        Query query = buildMultiMatchQuery(keyword, fieldNames);
        SearchHits<T> searchHits = elasticsearchOperations.search(query, documentClass);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public Page<T> searchByKeyword(String keyword, Pageable pageable, String... fieldNames) {
        Query query = buildMultiMatchQuery(keyword, fieldNames);
        query.setPageable(pageable);

        SearchHits<T> searchHits = elasticsearchOperations.search(query, documentClass);

        List<T> content = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, searchHits.getTotalHits());
    }

    @Override
    public SearchHits<T> search(Query query) {
        return elasticsearchOperations.search(query, documentClass);
    }

    /**
     * 构建多字段匹配查询
     * @param keyword 关键字
     * @param fieldNames 字段名列表
     * @return 查询对象
     */
    protected abstract Query buildMultiMatchQuery(String keyword, String... fieldNames);

    /**
     * 获取索引名称
     * @return 索引名称
     */
    protected abstract String getIndexName();
} 