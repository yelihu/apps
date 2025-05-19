package com.example.app1.elasticsearch.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Elasticsearch文档基类，包含所有文档共有的基础字段
 */
@Data
public abstract class BaseDocument implements Serializable {

    @Id
    private String id;

    @Field(type = FieldType.Date, name = "create_time", format = DateFormat.date_hour_minute_second)
    private LocalDateTime createTime;

    @Field(type = FieldType.Date, name = "update_time", format = DateFormat.date_hour_minute_second)
    private LocalDateTime updateTime;

    @Field(type = FieldType.Keyword, name = "creator")
    private String creator;

    @Field(type = FieldType.Keyword, name = "updater")
    private String updater;

    /**
     * 初始化创建信息
     */
    public void initCreate(String creator) {
        this.creator = creator;
        this.updater = creator;
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }

    /**
     * 更新修改信息
     */
    public void updateInfo(String updater) {
        this.updater = updater;
        this.updateTime = LocalDateTime.now();
    }
} 