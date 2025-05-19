package com.example.app1.elasticsearch.service;

import com.example.app1.elasticsearch.service.impl.BaseElasticsearchServiceImpl;
import com.example.app1.elasticsearch.util.ElasticsearchQueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 人员文档服务实现类
 */
@Slf4j
@Service
public class PersonDocumentServiceImpl extends BaseElasticsearchServiceImpl<PersonDocument> {

    /**
     * 搜索字段：姓名、部门名称、职位
     */
    private static final String[] SEARCH_FIELDS = {"name", "department_name", "position"};

    public PersonDocumentServiceImpl(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    @Override
    protected Query buildMultiMatchQuery(String keyword, String... fieldNames) {
        // 如果未提供字段，则使用默认搜索字段
        if (fieldNames == null || fieldNames.length == 0) {
            fieldNames = SEARCH_FIELDS;
        }
        return ElasticsearchQueryBuilder.buildMultiMatchQuery(keyword, fieldNames);
    }

    @Override
    protected String getIndexName() {
        Document document = PersonDocument.class.getAnnotation(Document.class);
        if (document != null) {
            return document.indexName();
        }
        return "person";
    }

    /**
     * 根据部门ID查询人员
     * @param departmentId 部门ID
     * @return 人员列表
     */
    public List<PersonDocument> findByDepartment(String departmentId) {
        return findByField("department_id", departmentId);
    }

    /**
     * 根据员工ID查询人员
     * @param employeeId 员工ID
     * @return 人员信息
     */
    public Optional<PersonDocument> findByEmployeeId(String employeeId) {
        List<PersonDocument> results = findByField("employee_id", employeeId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    /**
     * 根据姓名前缀搜索人员
     * @param namePrefix 姓名前缀
     * @return 人员列表
     */
    public List<PersonDocument> findByNamePrefix(String namePrefix) {
        Query query = ElasticsearchQueryBuilder.buildPrefixQuery("name", namePrefix);
        SearchHits<PersonDocument> searchHits = elasticsearchOperations.search(query, PersonDocument.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据姓名进行全文匹配搜索
     * @param name 姓名关键字
     * @return 人员列表
     */
    public List<PersonDocument> findByNameMatching(String name) {
        Query query = ElasticsearchQueryBuilder.buildMatchQuery("name", name);
        SearchHits<PersonDocument> searchHits = elasticsearchOperations.search(query, PersonDocument.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
    
    /**
     * 搜索指定部门下的特定职位人员
     * @param departmentId 部门ID
     * @param position 职位关键字
     * @return 人员列表
     */
    public List<PersonDocument> searchByDepartmentAndPosition(String departmentId, String position) {
        // 构建查询
        String queryString = String.format(
                "{\"query\":{\"bool\":{\"must\":[" +
                "{\"term\":{\"department_id\":\"%s\"}}," +
                "{\"match\":{\"position\":{\"query\":\"%s\",\"analyzer\":\"ik_smart\"}}}" +
                "]}}}",
                departmentId, ElasticsearchQueryBuilder.escapeJsonString(position));
        
        SearchHits<PersonDocument> searchHits = elasticsearchOperations.search(
                new org.springframework.data.elasticsearch.core.query.StringQuery(queryString),
                PersonDocument.class);
        
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
} 