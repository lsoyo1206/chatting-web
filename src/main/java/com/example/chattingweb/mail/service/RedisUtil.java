package com.example.chattingweb.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;//Redis에 접근하기 위한 Spring의 Redis 템플릿 클래스

    // 지정된 key 에 해당하는 value 값을 redis에서 가져오는 메소드
    public String getData(String key){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    // 지정된 key 에 값을 저장하는 메소드
    public void setData(String key, String value){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,value);
    }

    // 지정된 key 에 값을 저장하고, 지정된 시간 후에 데이터가 만료되도록 하는 메소드
    public void setDataExpire(String key, String value, long duration){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration); //지정된 시간

        //key - 인증번호 , value - 이메일
        valueOperations.set(key, value, expireDuration);

    }

    // 지정된 key 에 해당되는 데이터를 Redis 에서 삭제
    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}
