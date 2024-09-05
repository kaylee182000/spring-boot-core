package com.springboot.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setTimeToLive(String key, long timeoutInDays) {
        redisTemplate.expire(key, timeoutInDays, TimeUnit.DAYS);
    }

    public void hashSet(String key, String field, Object value) {
        hashOperations.put(key, field, value);
    }

    public boolean hashExists(String key, String field) {

        return hashOperations.hasKey(key, field);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Map<String, Object> getField(String key) {
        return hashOperations.entries(key);
    }

    public Object hashGet(String key, String field) {
        return hashOperations.get(key, field);
    }

    public List<Object> hashGetByFieldPrefix(String key, String filedPrefix) {
        List<Object> objects = new ArrayList<>();

        Map<String, Object> hashEntries = hashOperations.entries(key);

        for (Map.Entry<String, Object> entry : hashEntries.entrySet()) {
            if (entry.getKey().startsWith(filedPrefix)) {
                objects.add(entry.getValue());
            }
        }
        return objects;
    }

    public Set<String> getFieldPrefixes(String key) {
        return hashOperations.entries(key).keySet();
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void delete(String key, String field) {
        hashOperations.delete(key, field);
    }

    public void delete(String key, List<String> fields) {
        for (String field : fields) {
            hashOperations.delete(key, field);
        }
    }
}
