package com.example.app1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.app1.entity.EmployeePo;
import com.example.app1.mapper.EmployeeMapper;
import com.example.app1.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, EmployeePo> implements EmployeeService {
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    @Override
    public EmployeePo getEmployeeByWorkNo(String workNo) {
        EmployeePo employee = employeeMapper.selectByWorkNo(workNo);
        if (employee == null) {
            throw new RuntimeException("员工不存在: " + workNo);
        }
        return employee;
    }
    
    @Override
    public List<EmployeePo> getEmployeesByDept(String deptNo) {
        return employeeMapper.selectByDeptNo(deptNo);
    }
    
    @Override
    public List<EmployeePo> searchEmployees(String keyword) {
        // 合并姓名和职位的搜索结果
        Set<EmployeePo> results = new HashSet<>();
        results.addAll(employeeMapper.selectByNameLike(keyword));
        results.addAll(employeeMapper.selectByPositionLike(keyword));
        return new ArrayList<>(results);
    }
    
    @Override
    public List<EmployeePo> getEmployeesByLevel(String level) {
        return employeeMapper.selectByLevel(level);
    }
    
    @Override
    public List<EmployeePo> getEmployeesByConditions(String deptNo, String position, String level) {
        Map<String, Object> params = new HashMap<>();
        params.put("deptNo", deptNo);
        params.put("position", position);
        params.put("level", level);
        return employeeMapper.selectByMultiCondition(params);
    }
} 