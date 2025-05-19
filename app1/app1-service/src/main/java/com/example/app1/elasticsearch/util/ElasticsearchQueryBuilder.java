package com.example.app1.elasticsearch.util;

import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Elasticsearch查询构建器，提供各种类型的查询构建方法
 * <p>
 * 此工具类封装了Elasticsearch的查询DSL构建逻辑，支持多种常见查询类型，如
 * 多字段匹配查询、精确匹配查询、前缀查询、范围查询等，使用户可以更方便地
 * 构建复杂的Elasticsearch查询。
 * </p>
 */
public class ElasticsearchQueryBuilder {

    /**
     * 构建多字段匹配查询（Multi Match Query）
     * <p>
     * 该查询类型允许同一个查询词在多个字段上执行全文搜索，适用于需要
     * 在多个文本字段中查找相同内容的场景。默认使用best_fields类型和
     * ik_smart分词器，以提供更精准的中文搜索。
     * </p>
     * 
     * <p>示例：搜索"工程师"关键词在name和position字段</p>
     * <pre>
     * Query query = ElasticsearchQueryBuilder.buildMultiMatchQuery("工程师", "name", "position");
     * </pre>
     *
     * @param keyword 搜索关键词，必须非空
     * @param fieldNames 要搜索的字段名数组，必须非空
     * @return 构建的查询对象，可直接用于ElasticsearchOperations.search方法
     * @throws IllegalArgumentException 如果keyword为空或fieldNames为空
     */
    public static Query buildMultiMatchQuery(String keyword, String... fieldNames) {
        Assert.hasText(keyword, "Keyword must not be empty");
        Assert.notEmpty(fieldNames, "Field names must not be empty");

        StringBuilder queryString = new StringBuilder("{\"multi_match\":{");
        queryString.append("\"query\":\"").append(escapeJsonString(keyword)).append("\",");
        queryString.append("\"fields\":[");
        
        for (int i = 0; i < fieldNames.length; i++) {
            queryString.append("\"").append(fieldNames[i]).append("\"");
            if (i < fieldNames.length - 1) {
                queryString.append(",");
            }
        }
        
        queryString.append("],");
        queryString.append("\"type\":\"best_fields\",");
        queryString.append("\"analyzer\":\"ik_smart\"");
        queryString.append("}}");
        
        return new StringQuery(queryString.toString());
    }
    
    /**
     * 转义JSON字符串中的特殊字符
     * <p>
     * 将输入字符串中的特殊字符（如双引号、反斜杠、换行符等）进行转义，
     * 以确保生成的JSON字符串是有效的。防止JSON注入和解析错误。
     * </p>
     *
     * @param input 需要转义的输入字符串，可以为null
     * @return 转义后的字符串，如果输入为null则返回空字符串
     */
    public static String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            switch (ch) {
                case '\"':
                    result.append("\\\"");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                case '\b':
                    result.append("\\b");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                default:
                    result.append(ch);
            }
        }
        return result.toString();
    }
    
    /**
     * 构建基于Criteria的单字段查询
     * <p>
     * 使用Spring Data Elasticsearch的Criteria API构建单字段精确匹配查询。
     * 相比StringQuery，CriteriaQuery提供了更面向对象的查询构建方式，
     * 尤其适合在需要动态构建查询条件的场景。
     * </p>
     * 
     * <p>示例：查询departmentId等于"DEP001"的文档</p>
     * <pre>
     * Query query = ElasticsearchQueryBuilder.buildCriteriaQuery("departmentId", "DEP001");
     * </pre>
     *
     * @param fieldName 字段名，必须非空
     * @param value 字段值，必须非null
     * @return 构建的CriteriaQuery查询对象
     * @throws IllegalArgumentException 如果fieldName为空或value为null
     */
    public static Query buildCriteriaQuery(String fieldName, Object value) {
        Assert.hasText(fieldName, "Field name must not be empty");
        Assert.notNull(value, "Value must not be null");

        Criteria criteria = new Criteria(fieldName).is(value);
        return new CriteriaQuery(criteria);
    }

    /**
     * 构建多字段条件查询
     * <p>
     * 基于多个字段名和对应值构建"AND"关系的查询条件。每个字段都会执行精确匹配，
     * 所有条件必须同时满足。这种查询适合需要多条件组合过滤的场景。
     * </p>
     * 
     * <p>示例：查询部门为"研发部"且职位等级为3的员工</p>
     * <pre>
     * Map<String, Object> conditions = new HashMap<>();
     * conditions.put("department", "研发部");
     * conditions.put("level", 3);
     * Query query = ElasticsearchQueryBuilder.buildMultiFieldCriteriaQuery(conditions);
     * </pre>
     *
     * @param fieldValueMap 字段名和值的映射Map，键为字段名，值为对应的字段值，必须非空
     * @return 构建的查询对象
     * @throws IllegalArgumentException 如果fieldValueMap为空
     */
    public static Query buildMultiFieldCriteriaQuery(Map<String, Object> fieldValueMap) {
        Assert.notEmpty(fieldValueMap, "Field value map must not be empty");

        Criteria criteria = null;
        boolean isFirst = true;

        for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
            if (isFirst) {
                criteria = new Criteria(entry.getKey()).is(entry.getValue());
                isFirst = false;
            } else {
                criteria = criteria.and(entry.getKey()).is(entry.getValue());
            }
        }

        return new CriteriaQuery(criteria);
    }
    
    /**
     * 构建精确匹配查询（Term Query）
     * <p>
     * 创建一个Term查询，用于精确匹配字段值。不同于全文搜索，Term查询不会对查询
     * 字符串进行分析，而是精确匹配字段的完整值。这种查询特别适合Keyword类型字段
     * 或需要精确匹配的场合。
     * </p>
     * 
     * <p>示例：查询employee_id为"EMP001"的员工</p>
     * <pre>
     * Query query = ElasticsearchQueryBuilder.buildTermQuery("employee_id", "EMP001");
     * </pre>
     *
     * @param field 要查询的字段名
     * @param value 要匹配的字段值
     * @return 构建的精确匹配查询对象
     */
    public static Query buildTermQuery(String field, Object value) {
        Assert.hasText(field, "Field name must not be empty");
        Assert.notNull(value, "Value must not be null");
        
        String queryString = String.format("{\"term\":{\"%s\":\"%s\"}}", 
                field, value.toString());
        return new StringQuery(queryString);
    }
    
    /**
     * 构建前缀查询（Prefix Query）
     * <p>
     * 创建一个前缀查询，用于查找字段值以指定前缀开头的文档。这种查询
     * 适合实现自动完成、搜索提示等功能。前缀查询对性能有一定影响，不建议
     * 使用过短的前缀。
     * </p>
     * 
     * <p>示例：查找名称以"张"开头的员工</p>
     * <pre>
     * Query query = ElasticsearchQueryBuilder.buildPrefixQuery("name", "张");
     * </pre>
     *
     * @param field 要查询的字段名
     * @param prefix 前缀值
     * @return 构建的前缀查询对象
     */
    public static Query buildPrefixQuery(String field, String prefix) {
        Assert.hasText(field, "Field name must not be empty");
        Assert.hasText(prefix, "Prefix must not be empty");
        
        String queryString = String.format("{\"prefix\":{\"%s\":\"%s\"}}", 
                field, escapeJsonString(prefix));
        return new StringQuery(queryString);
    }
    
    /**
     * 构建范围查询（Range Query）
     * <p>
     * 创建一个范围查询，用于查找字段值在指定范围内的文档。可以指定上下限，
     * 也可以只指定一个。这种查询特别适合日期、数字等有序数据的范围筛选。
     * </p>
     * 
     * <p>示例1：查询价格在100到200之间的产品</p>
     * <pre>
     * Query query = ElasticsearchQueryBuilder.buildRangeQuery("price", 100, 200);
     * </pre>
     * 
     * <p>示例2：查询2023年以后创建的文档</p>
     * <pre>
     * Query query = ElasticsearchQueryBuilder.buildRangeQuery("create_time", "2023-01-01", null);
     * </pre>
     *
     * @param field 要查询的字段名
     * @param from 范围的下限值，可以为null表示无下限
     * @param to 范围的上限值，可以为null表示无上限
     * @return 构建的范围查询对象
     */
    public static Query buildRangeQuery(String field, Object from, Object to) {
        Assert.hasText(field, "Field name must not be empty");
        Assert.isTrue(from != null || to != null, "At least one of from or to must be specified");
        
        StringBuilder rangeQuery = new StringBuilder();
        rangeQuery.append("{\"range\":{\"").append(field).append("\":{");
        
        if (from != null) {
            rangeQuery.append("\"gte\":\"").append(from).append("\"");
            if (to != null) {
                rangeQuery.append(",");
            }
        }
        
        if (to != null) {
            rangeQuery.append("\"lte\":\"").append(to).append("\"");
        }
        
        rangeQuery.append("}}}");
        return new StringQuery(rangeQuery.toString());
    }
    
    /**
     * 构建全文匹配查询（Match Query）
     * <p>
     * 创建一个全文匹配查询，用于在文本字段中搜索关键词。该查询会对搜索文本
     * 进行分析（使用ik_smart分词器），然后匹配包含分析后词条的文档。这是全文
     * 搜索的基础查询类型，特别适合搜索中文内容。
     * </p>
     * 
     * <p>示例：在职位描述中搜索包含"工程师"的文档</p>
     * <pre>
     * Query query = ElasticsearchQueryBuilder.buildMatchQuery("position", "高级工程师");
     * </pre>
     *
     * @param field 要搜索的文本字段名
     * @param text 搜索文本，将被分词器处理
     * @return 构建的全文匹配查询对象
     */
    public static Query buildMatchQuery(String field, String text) {
        Assert.hasText(field, "Field name must not be empty");
        Assert.hasText(text, "Search text must not be empty");
        
        String queryString = String.format(
                "{\"match\":{\"%s\":{\"query\":\"%s\",\"analyzer\":\"ik_smart\"}}}",
                field, escapeJsonString(text));
        return new StringQuery(queryString);
    }
    
    /**
     * 构建布尔查询（Bool Query）
     * <p>
     * 创建一个布尔查询，组合多个查询条件。可以添加must、should、must_not等子句。
     * 这是构建复杂查询的基础，允许组合各种查询类型。
     * </p>
     * 
     * <p>示例：查询职位包含"工程师"且部门为"研发部"的员工</p>
     * <pre>
     * // 创建子查询的JSON字符串
     * String matchQuery = "{\"match\":{\"position\":{\"query\":\"工程师\",\"analyzer\":\"ik_smart\"}}}";
     * String termQuery = "{\"term\":{\"department_name\":\"研发部\"}}";
     * 
     * // 构建bool查询
     * Query query = ElasticsearchQueryBuilder.buildBoolQuery()
     *     .must(matchQuery)
     *     .must(termQuery)
     *     .build();
     * </pre>
     *
     * @return 布尔查询构建器
     */
    public static BoolQueryBuilder buildBoolQuery() {
        return new BoolQueryBuilder();
    }
    
    /**
     * 布尔查询构建器，用于构建复杂的组合查询
     */
    public static class BoolQueryBuilder {
        private final StringBuilder queryBuilder;
        private boolean hasMust = false;
        private boolean hasShould = false;
        private boolean hasMustNot = false;
        private boolean hasFilter = false;
        
        private BoolQueryBuilder() {
            this.queryBuilder = new StringBuilder("{\"bool\":{");
        }
        
        /**
         * 添加必须匹配的条件（AND）
         * @param queryString 子查询JSON字符串
         * @return 当前构建器
         */
        public BoolQueryBuilder must(String queryString) {
            if (queryString != null && !queryString.isEmpty()) {
                if (!hasMust) {
                    queryBuilder.append("\"must\":[");
                    hasMust = true;
                } else {
                    queryBuilder.append(",");
                }
                queryBuilder.append(queryString);
            }
            return this;
        }
        
        /**
         * 添加应该匹配的条件（OR）
         * @param queryString 子查询JSON字符串
         * @return 当前构建器
         */
        public BoolQueryBuilder should(String queryString) {
            if (queryString != null && !queryString.isEmpty()) {
                if (!hasShould) {
                    if (hasMust) {
                        queryBuilder.append("],");
                    }
                    queryBuilder.append("\"should\":[");
                    hasShould = true;
                } else {
                    queryBuilder.append(",");
                }
                queryBuilder.append(queryString);
            }
            return this;
        }
        
        /**
         * 添加不能匹配的条件（NOT）
         * @param queryString 子查询JSON字符串
         * @return 当前构建器
         */
        public BoolQueryBuilder mustNot(String queryString) {
            if (queryString != null && !queryString.isEmpty()) {
                if (!hasMustNot) {
                    if (hasMust) {
                        queryBuilder.append("],");
                    }
                    if (hasShould) {
                        queryBuilder.append("],");
                    }
                    queryBuilder.append("\"must_not\":[");
                    hasMustNot = true;
                } else {
                    queryBuilder.append(",");
                }
                queryBuilder.append(queryString);
            }
            return this;
        }
        
        /**
         * 添加过滤条件（不参与相关性评分计算）
         * @param queryString 子查询JSON字符串
         * @return 当前构建器
         */
        public BoolQueryBuilder filter(String queryString) {
            if (queryString != null && !queryString.isEmpty()) {
                if (!hasFilter) {
                    if (hasMust) {
                        queryBuilder.append("],");
                    }
                    if (hasShould) {
                        queryBuilder.append("],");
                    }
                    if (hasMustNot) {
                        queryBuilder.append("],");
                    }
                    queryBuilder.append("\"filter\":[");
                    hasFilter = true;
                } else {
                    queryBuilder.append(",");
                }
                queryBuilder.append(queryString);
            }
            return this;
        }
        
        /**
         * 构建查询对象
         * @return 布尔查询对象
         */
        public Query build() {
            if (hasMust) {
                queryBuilder.append("]");
            }
            if (hasShould) {
                queryBuilder.append("]");
            }
            if (hasMustNot) {
                queryBuilder.append("]");
            }
            if (hasFilter) {
                queryBuilder.append("]");
            }
            queryBuilder.append("}}");
            return new StringQuery(queryBuilder.toString());
        }
    }
} 