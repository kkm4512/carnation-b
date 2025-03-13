package com.example.carnation.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration  // Spring 설정 클래스임을 나타냄
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // Redis와의 연결을 설정 (RedisConnectionFactory를 사용하여 Redis 서버와 연결)
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // Custom ObjectMapper 생성 (JSON 직렬화 방식 설정)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Java 8 날짜/시간(LocalDateTime) 지원 추가
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);  // 날짜 데이터를 문자열(ISO-8601 형식)로 저장
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);  // 모든 필드를 JSON 직렬화 대상에 포함

        // Key Serializer 설정 (Redis에서 Key는 일반 문자열이므로 StringRedisSerializer 사용)
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // Value Serializer 설정 (값을 JSON 형식으로 저장, ObjectMapper를 사용하여 직렬화)
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        // Hash Key Serializer 설정 (Redis Hash 구조의 키도 문자열로 저장)
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Hash Value Serializer 설정 (Hash의 값도 JSON 형식으로 저장)
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        // 모든 설정 적용 후 RedisTemplate을 초기화
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
