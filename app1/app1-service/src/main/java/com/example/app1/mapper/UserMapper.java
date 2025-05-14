package com.example.app1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.app1.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承BaseMapper后即可使用MyBatis-Plus提供的通用CRUD方法
} 