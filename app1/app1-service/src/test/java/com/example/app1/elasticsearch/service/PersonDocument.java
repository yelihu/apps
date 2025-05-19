package com.example.app1.elasticsearch.service;

import com.example.app1.elasticsearch.domain.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * 人员文档，用于搜索人员信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "person")
@Setting(shards = 1, replicas = 0, refreshInterval = "1s")
public class PersonDocument extends BaseDocument {

    /**
     * 姓名，使用Text类型支持分词搜索
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart", name = "name")
    private String name;

    /**
     * 姓名排序字段，使用Keyword类型支持排序
     */
    @Field(type = FieldType.Keyword, name = "name_sort")
    private String nameSort;

    /**
     * 工号，使用Keyword类型进行精确匹配
     */
    @Field(type = FieldType.Keyword, name = "employee_id")
    private String employeeId;

    /**
     * 部门ID
     */
    @Field(type = FieldType.Keyword, name = "department_id")
    private String departmentId;

    /**
     * 部门名称，使用Text类型支持分词搜索
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart", name = "department_name")
    private String departmentName;

    /**
     * 职位，使用Text类型支持分词搜索
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart", name = "position")
    private String position;

    /**
     * 邮箱
     */
    @Field(type = FieldType.Keyword, name = "email")
    private String email;

    /**
     * 手机号
     */
    @Field(type = FieldType.Keyword, name = "mobile")
    private String mobile;
} 