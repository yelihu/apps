package com.example.app1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.app1.entity.DepartmentPo;

import java.util.List;

public interface DepartmentService extends IService<DepartmentPo> {
    
    /**
     * 根据部门编号查询部门信息
     */
    DepartmentPo getDepartmentByDeptNo(String deptNo);
    
    /**
     * 查询部门及其所有子部门
     */
    List<DepartmentPo> getDeptWithChildren(String deptNo);
    
    /**
     * 搜索部门（根据部门名称）
     */
    List<DepartmentPo> searchDepartments(String keyword);
} 