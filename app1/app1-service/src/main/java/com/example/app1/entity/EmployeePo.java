package com.example.app1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("emp_basic")
public class EmployeePo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String workNo;
    private String name;
    private String gender;
    private Date birthDate;
    private String deptNo;
    private Date entryDate;
    private String position;
    private String level;
    private Date gmtCreate;
    private Date gmtModified;
} 