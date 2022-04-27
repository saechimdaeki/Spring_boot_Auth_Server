package saechimdaeki.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void setRefreshToken(String userName, String token){
        ValueOperations<String, String> redisValues = redisTemplate.opsForValue();
        redisValues.set(token, userName, Duration.ofDays(7)); // 7일 뒤 refreshToken 만료
    }

    public String getUserName(String token){
        ValueOperations<String, String> redisValues = redisTemplate.opsForValue();
        return redisValues.get(token);
    }

    public void deleteToken(String token){
        redisTemplate.delete(token);
    }

    public boolean isValidKey(String token){
        return redisTemplate.hasKey(token);
    }
}
