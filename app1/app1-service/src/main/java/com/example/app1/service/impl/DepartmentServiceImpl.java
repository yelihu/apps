package com.example.app1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.app1.entity.DepartmentPo;
import com.example.app1.mapper.DepartmentMapper;
import com.example.app1.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, DepartmentPo> implements DepartmentService {
    
    @Autowired
    private DepartmentMapper departmentMapper;
    
    @Override
    public DepartmentPo getDepartmentByDeptNo(String deptNo) {
        DepartmentPo department = departmentMapper.selectByDeptNo(deptNo);
        if (department == null) {
            throw new RuntimeException("部门不存在: " + deptNo);
        }
        return department;
    }
    
    @Override
    public List<DepartmentPo> getDeptWithChildren(String deptNo) {
        List<DepartmentPo> result = new ArrayList<>();
        // 添加当前部门
        DepartmentPo department = getDepartmentByDeptNo(deptNo);
        result.add(department);
        // 添加所有子部门
        result.addAll(departmentMapper.selectByParentDept(deptNo));
        return result;
    }
    
    @Override
    public List<DepartmentPo> searchDepartments(String keyword) {
        return departmentMapper.selectByDeptNameLike(keyword);
    }
} 