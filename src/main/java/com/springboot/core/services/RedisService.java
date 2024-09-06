package com.springboot.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService<K, F, V> {
    private final RedisTemplate<K, V> redisTemplate;
    private final HashOperations<K, F, V> hashOperations;

    // public RedisService(RedisTemplate<K, V> redisTemplate) {
    // this.redisTemplate = redisTemplate;
    // this.hashOperations = redisTemplate.opsForHash();
    // }

    public void set(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setTimeToLive(K key, long timeoutInDays) {
        redisTemplate.expire(key, timeoutInDays, TimeUnit.DAYS);
    }

    public void hashSet(K key, F field, V value) {
        hashOperations.put(key, field, value);
    }

    public boolean hashExists(K key, F field) {

        return hashOperations.hasKey(key, field);
    }

    public Object get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Map<F, V> getField(K key) {
        return hashOperations.entries(key);
    }

    public Object hashGet(K key, F field) {
        return hashOperations.get(key, field);
    }

    // public List<Object> hashGetByFieldPrefix(K key, F filedPrefix) {
    // List<Object> objects = new ArrayList<>();

    // Map<F, V> hashEntries = hashOperations.entries(key);

    // for (Map.Entry<F, V> entry : hashEntries.entrySet()) {
    // if (entry.getKey().startsWith(filedPrefix)) {
    // objects.add(entry.getValue());
    // }
    // }
    // return objects;
    // }

    public Set<F> getFieldPrefixes(K key) {
        return hashOperations.entries(key).keySet();
    }

    public void delete(K key) {
        redisTemplate.delete(key);
    }

    public void delete(K key, F field) {
        hashOperations.delete(key, field);
    }

    public void delete(K key, List<F> fields) {
        for (F field : fields) {
            hashOperations.delete(key, field);
        }
    }
}
