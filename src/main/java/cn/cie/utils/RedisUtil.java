package cn.cie.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public void setString(String key, String value){
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String getString(String key){
        String s = stringRedisTemplate.opsForValue().get(key);
        return s;
    }

    public <T> void setObj(String key, T obj){
        try {
            String value = MAPPER.writeValueAsString(obj);
            stringRedisTemplate.opsForValue().set(key, value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getObj(String key, T obj){
        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            obj = (T)MAPPER.readValue(value, obj.getClass());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }


    public void setStringEx(String key, String value, int timeout){
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofMinutes(timeout));
    }


    public <T> void setObjEx(String key, T obj, int timeout){
        try {
            String value = MAPPER.writeValueAsString(obj);
            stringRedisTemplate.opsForValue().set(key, value, Duration.ofMinutes(timeout));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



}
