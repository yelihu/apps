package com.example.app1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("dept_info")
public class DepartmentPo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String deptNo;
    private String deptName;
    private String mgrNo;
    private String mgrName;
    private Date createDate;
    private String parentDept;
    private Integer deptLevel;
    private Date gmtCreate;
    private Date gmtModified;
} 