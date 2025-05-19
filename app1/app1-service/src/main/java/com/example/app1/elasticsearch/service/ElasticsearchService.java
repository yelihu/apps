package com.example.app1.elasticsearch.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Elasticsearch服务接口，提供基础的ES操作
 * @param <T> 文档类型
 */
public interface ElasticsearchService<T> {

    /**
     * 创建索引
     */
    void createIndex();

    /**
     * 删除索引
     */
    void deleteIndex();

    /**
     * 判断索引是否存在
     * @return 是否存在
     */
    boolean indexExists();

    /**
     * 保存文档
     * @param document 文档对象
     * @return 保存后的文档
     */
    T save(T document);

    /**
     * 批量保存文档
     * @param documents 文档列表
     * @return 保存后的文档列表
     */
    Iterable<T> saveAll(List<T> documents);

    /**
     * 根据ID查询文档
     * @param id 文档ID
     * @return 查询结果
     */
    Optional<T> findById(String id);

    /**
     * 根据ID删除文档
     * @param id 文档ID
     */
    void deleteById(String id);

    /**
     * 删除所有文档
     */
    void deleteAll();

    /**
     * 根据字段精确查询
     * @param fieldName 字段名
     * @param value 字段值
     * @return 查询结果
     */
    List<T> findByField(String fieldName, Object value);

    /**
     * 根据多个字段精确查询
     * @param fieldValues 字段名和值的Map
     * @return 查询结果
     */
    List<T> findByFields(Map<String, Object> fieldValues);

    /**
     * 关键字搜索（多字段模糊查询）
     * @param keyword 关键字
     * @param fieldNames 字段名列表
     * @return 搜索结果
     */
    List<T> searchByKeyword(String keyword, String... fieldNames);

    /**
     * 关键字搜索（多字段模糊查询），带分页
     * @param keyword 关键字
     * @param pageable 分页参数
     * @param fieldNames 字段名列表
     * @return 分页搜索结果
     */
    Page<T> searchByKeyword(String keyword, Pageable pageable, String... fieldNames);

    /**
     * 高级搜索，使用Query对象
     * @param query 查询对象
     * @return 搜索结果
     */
    SearchHits<T> search(Query query);
} 