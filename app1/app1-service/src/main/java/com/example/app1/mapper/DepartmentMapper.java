package com.example.app1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.app1.entity.DepartmentPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<DepartmentPo> {
    
    @Select("SELECT * FROM dept_info WHERE dept_no = #{deptNo}")
    DepartmentPo selectByDeptNo(@Param("deptNo") String deptNo);
    
    @Select("SELECT * FROM dept_info WHERE dept_name LIKE CONCAT('%', #{deptName}, '%')")
    List<DepartmentPo> selectByDeptNameLike(@Param("deptName") String deptName);
    
    @Select("SELECT * FROM dept_info WHERE parent_dept = #{parentDept}")
    List<DepartmentPo> selectByParentDept(@Param("parentDept") String parentDept);
    
    @Select("SELECT * FROM dept_info WHERE mgr_no = #{mgrNo}")
    DepartmentPo selectByMgrNo(@Param("mgrNo") String mgrNo);
} 