package com.example.app1;

import com.example.app1.entity.User;
import com.example.app1.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 用户服务测试类
 */
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    /**
     * 测试查询所有用户
     */
    @Test
    public void testListAllUsers() {
        List<User> users = userService.list();
        assertNotNull(users);
        assertTrue(users.size() > 0);
        users.forEach(user -> System.out.println(user.toString()));
    }

    @Test
    public void updateSelective() {
        User user = new User();
        user.setId(1L);
        user.setRealName("管理员");
        userService.updateById(user);
    }
} 