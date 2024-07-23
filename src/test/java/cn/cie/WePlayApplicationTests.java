package cn.cie;

import cn.cie.entity.User;
import cn.cie.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WePlayApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println("测试成功");
    }

    @Test
    void userMapper(){

        User user = userMapper.selectById(1);
        System.out.println(user);
    }

}
