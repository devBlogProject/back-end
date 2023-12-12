package com.multi.blogging.multiblogging.infra.redisDb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setValues(String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return (String) values.get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public void expireValues(String key, int timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public void setHashOps(String key, Map<String, String> data) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        values.putAll(key, data);
    }

    @Transactional(readOnly = true)
    public String getHashOps(String key, String hashKey) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        return Boolean.TRUE.equals(values.hasKey(key, hashKey)) ? (String) redisTemplate.opsForHash().get(key, hashKey) : "";
    }

    @Transactional(readOnly = true)
    public Map<Object, Object> getHashData(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    public void deleteHashOps(String key, String hashKey) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        values.delete(key, hashKey);
    }

    public void setSetOps(String key, String setValue) {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.add(key, setValue);
    }

    @Transactional(readOnly = true)
    public Set<Object> getSetOps(String key){
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        return setOperations.members(key);
    }

    @Transactional(readOnly = true)
    public Map<String,Set> getKeyAndSetOpsContainPrefix(String prefix){
        Map<String, Set> keyAndSetMap = new HashMap<>();
        ScanOptions options = ScanOptions.scanOptions().match(prefix+"*").build();
        Cursor<byte[]> scan = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().scan(options);
        while (scan.hasNext()) {
            byte[] next = scan.next();
            String key = new String(next, StandardCharsets.UTF_8);
            keyAndSetMap.put(key, getSetOps(key));
        }

        return keyAndSetMap;
    }

    public void deleteKeyByContainPrefix(String prefix){
        ScanOptions options = ScanOptions.scanOptions().match(prefix+"*").build();
        Cursor<byte[]> scan = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().scan(options);
        while (scan.hasNext()){
            byte[] next = scan.next();
            String key = new String(next, StandardCharsets.UTF_8);
            redisTemplate.delete(key);
        }
    }

    public void deleteSetOps(String key,String value){
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.remove(key, value);
    }

    public boolean checkExistsValue(String value) {
        return !value.equals("false");
    }
}
