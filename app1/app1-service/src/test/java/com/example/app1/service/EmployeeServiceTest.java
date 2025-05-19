package com.example.app1.service;

import com.example.app1.entity.EmployeePo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    @DisplayName("测试根据工号查询员工")
    public void testGetEmployeeByWorkNo() {
        // 根据SQL文件中的示例数据测试
        EmployeePo employee = employeeService.getEmployeeByWorkNo("EMP_001");
        
        assertNotNull(employee);
        assertEquals("Jackpot", employee.getName());
        assertEquals("001", employee.getDeptNo());
        assertEquals("首席执行官", employee.getPosition());
    }

    @Test
    @DisplayName("测试根据部门编号查询员工")
    public void testGetEmployeesByDept() {
        // 查询销售部门员工
        List<EmployeePo> employees = employeeService.getEmployeesByDept("003");
        
        assertNotNull(employees);
        assertFalse(employees.isEmpty());
        // 根据SQL示例数据，003部门应该有4人（包括部门主管）
        assertEquals(4, employees.size());
        
        // 验证部门员工都是003部门的
        for (EmployeePo employee : employees) {
            assertEquals("003", employee.getDeptNo());
        }
    }

    @Test
    @DisplayName("测试搜索员工")
    public void testSearchEmployees() {
        // 测试根据姓名关键字搜索
        List<EmployeePo> employeesByName = employeeService.searchEmployees("张");
        assertNotNull(employeesByName);
        assertTrue(employeesByName.size() > 0);
        
        // 测试根据职位关键字搜索
        List<EmployeePo> employeesByPosition = employeeService.searchEmployees("销售");
        assertNotNull(employeesByPosition);
        assertTrue(employeesByPosition.size() > 0);
    }

    @Test
    @DisplayName("测试根据职级查询员工")
    public void testGetEmployeesByLevel() {
        // 查询E3级别的员工
        List<EmployeePo> e3Employees = employeeService.getEmployeesByLevel("E3");
        
        assertNotNull(e3Employees);
        assertFalse(e3Employees.isEmpty());
        
        // 验证所有返回的员工都是E3级别
        for (EmployeePo employee : e3Employees) {
            assertEquals("E3", employee.getLevel());
        }
    }

    @Test
    @DisplayName("测试根据多条件查询员工")
    public void testGetEmployeesByConditions() {
        // 测试在销售部门(003)查找销售经理职位的员工
        List<EmployeePo> salesManagers = employeeService.getEmployeesByConditions("003", "经理", null);
        
        assertNotNull(salesManagers);
        assertTrue(salesManagers.size() > 0);
        
        // 验证查询结果
        for (EmployeePo employee : salesManagers) {
            assertEquals("003", employee.getDeptNo());
            assertTrue(employee.getPosition().contains("经理"));
        }
    }

    @Test
    @DisplayName("测试查询不存在的工号")
    public void testGetNonExistentEmployee() {
        // 测试查询不存在的员工
        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.getEmployeeByWorkNo("NONEXIST_EMP");
        });
        
        assertTrue(exception.getMessage().contains("员工不存在"));
    }
} 