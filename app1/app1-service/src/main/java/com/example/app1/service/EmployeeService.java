package com.example.app1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.app1.entity.EmployeePo;

import java.util.List;
import java.util.Map;

public interface EmployeeService extends IService<EmployeePo> {
    
    /**
     * 根据工号查询员工信息
     */
    EmployeePo getEmployeeByWorkNo(String workNo);
    
    /**
     * 根据部门编号查询所有员工
     */
    List<EmployeePo> getEmployeesByDept(String deptNo);
    
    /**
     * 搜索员工（根据姓名或职位）
     */
    List<EmployeePo> searchEmployees(String keyword);
    
    /**
     * 根据职级查询员工列表
     */
    List<EmployeePo> getEmployeesByLevel(String level);
    
    /**
     * 根据多条件查询员工
     */
    List<EmployeePo> getEmployeesByConditions(String deptNo, String position, String level);
} 