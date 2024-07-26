package cn.cie;

import cn.cie.entity.User;
import cn.cie.mapper.UserMapper;
import cn.cie.utils.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@SpringBootTest
class WePlayApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {
        System.out.println("测试成功");
    }

    @Test
    void userMapper(){

        User user = userMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    void testString(){
        stringRedisTemplate.opsForValue().set("name", "zhangsan");
        String name = stringRedisTemplate.opsForValue().get("name");
        System.out.println(name);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Test
    void testSaveUser() throws JsonProcessingException {
        User user = userMapper.selectById(1);
        String s = MAPPER.writeValueAsString(user);
        stringRedisTemplate.opsForValue().set("user:1", s);
        String s1 = stringRedisTemplate.opsForValue().get("user:1");
        User user1 = MAPPER.readValue(s1, User.class);
        System.out.println(user1);
    }

    @Test
    void testRedisUtil(){
        User user = userMapper.selectById(1);
        redisUtil.setObj("user:1", user);
        User obj = redisUtil.getObj("user:1", user);
        System.out.println(obj);
    }

    @Test
    void testEx(){
        redisUtil.setStringEx("name:3", "maliu", 1);
    }

}
