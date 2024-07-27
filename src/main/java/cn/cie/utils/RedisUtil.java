package cn.cie.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class RedisUtil<T> {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 存放一条数据
     *
     * @param key
     * @param value
     * @return
     */
    public void setString(String key, String value){
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */
    public String getString(String key){
        String s = stringRedisTemplate.opsForValue().get(key);
        return s;
    }

    /**
     * 存放一个对象
     *
     * @param key
     * @param obj
     * @return
     */
    public void setObj(String key, T obj){
        try {
            String value = MAPPER.writeValueAsString(obj);
            stringRedisTemplate.opsForValue().set(key, value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据key获取对应的对象
     *
     * @param key
     * @return
     */
    public T getObj(String key, Class clazz){
        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            return  (T)MAPPER.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 存放一条定时过期的数据
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public void setStringEx(String key, String value, int timeout){
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeout));
    }


    /**
     * 存放一个定时过期的对象
     *
     * @param key
     * @param obj
     * @param timeout
     * @return
     */
    public void setObjEx(String key, T obj, int timeout){
        try {
            String value = MAPPER.writeValueAsString(obj);
            stringRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeout));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据key删除数据
     *
     * @param key
     * @return
     */
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }


    /**
     * 从队列头部出队一个元素，如果没有，则会阻塞 timeout 秒后返回null
     * 如果 timeout 为0，那么会一直阻塞直到有元素
     *
     * @param timeout 阻塞的时间，单位为秒
     * @param key
     * @param clazz
     * @return
     */
    public T blpopObject(int timeout, String key, Class clazz) {
        String value = stringRedisTemplate.opsForList().leftPop(key, Duration.ofSeconds(timeout));
        try {
            return  (T)MAPPER.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从队列左边出队一个元素
     *
     * @param key
     * @param clazz
     * @return
     */
    public T lpopObject(String key, Class clazz) {
        String value = stringRedisTemplate.opsForList().leftPop(key);
        try {
            return  (T)MAPPER.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向列表尾部添加数据
     *
     * @param key
     * @param values
     * @return
     */
    public long rpushObject(String key, Class clazz, Object... values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        String[] jsonStrs = new String[values.length];
        int index = 0;
        for (Object value : values) {
            try {
                jsonStrs[index] = MAPPER.writeValueAsString(value);
                ++index;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return stringRedisTemplate.opsForList().rightPushAll(key, jsonStrs);
    }


    /**
     * 向列表尾部添加某个时间点删除的数据
     *
     * @param key
     * @param time   unix时间戳
     * @param values
     * @return
     */
    public long rpushObjectExAtTime(String key, Class clazz, long time, Object... values) {
        if (values.length == 0) {
            return 0;
        }

        String[] jsonStrs = new String[values.length];
        int index = 0;
        for (Object value : values) {
            try {
                jsonStrs[index] = MAPPER.writeValueAsString(value);
                ++index;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        Long res = stringRedisTemplate.opsForList().rightPushAll(key, jsonStrs);
        stringRedisTemplate.expireAt(key, new Date(time));
        return res;
    }


    /**
     * 在列表尾部添加一个定期删除的数据
     *
     * @param key
     * @param clazz
     * @param timeout
     * @param values
     * @return
     */
    public long rpushObjectEx(String key, Class clazz, int timeout, Object... values) {
        if (values.length == 0) {
            return 0;
        }

        String[] jsonStrs = new String[values.length];
        int index = 0;
        for (Object value : values) {
            try {
                jsonStrs[index] = MAPPER.writeValueAsString(value);
                ++index;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        Long res = stringRedisTemplate.opsForList().rightPushAll(key, jsonStrs);
        stringRedisTemplate.expire(key, Duration.ofSeconds(timeout));
        return res;
    }


    /**
     * 获取列表中所有数据,ruguo
     *
     * @param key
     * @return
     */
    public List<T> lall(String key, Class clazz) {
        List<String> list = stringRedisTemplate.opsForList().range(key, 0, -1);
        List<T> res = new ArrayList<T>();
        if (list == null || list.size() == 0) {
            return res;
        }
        for (String str : list) {
            try {
                res.add((T) MAPPER.readValue(str, clazz));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }


}
