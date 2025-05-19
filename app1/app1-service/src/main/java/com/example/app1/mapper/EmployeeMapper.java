package com.example.app1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.app1.entity.EmployeePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmployeeMapper extends BaseMapper<EmployeePo> {
    
    @Select("SELECT * FROM emp_basic WHERE work_no = #{workNo}")
    EmployeePo selectByWorkNo(@Param("workNo") String workNo);
    
    @Select("SELECT * FROM emp_basic WHERE dept_no = #{deptNo}")
    List<EmployeePo> selectByDeptNo(@Param("deptNo") String deptNo);
    
    @Select("SELECT * FROM emp_basic WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<EmployeePo> selectByNameLike(@Param("name") String name);
    
    @Select("SELECT * FROM emp_basic WHERE position LIKE CONCAT('%', #{position}, '%')")
    List<EmployeePo> selectByPositionLike(@Param("position") String position);
    
    /**
     * 根据职级查询员工列表（XML方式实现）
     */
    List<EmployeePo> selectByLevel(@Param("level") String level);
    
    /**
     * 根据多个条件组合查询员工（XML方式实现）
     */
    List<EmployeePo> selectByMultiCondition(Map<String, Object> params);
} 