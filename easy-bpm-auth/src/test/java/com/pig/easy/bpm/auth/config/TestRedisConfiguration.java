package com.pig.easy.bpm.auth.config;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnProperty(name = "redis.enabled", havingValue = "false")
public class TestRedisConfiguration {

    @Bean
    @Primary
    @SuppressWarnings("unchecked")
    public RedisTemplate<Object, Object> redisTemplate() {
        return Mockito.mock(RedisTemplate.class);
    }
}
