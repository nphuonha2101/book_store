package com.ecommerce.book_store.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfiguration {
    @Bean
    public RedisConnectionFactory redisConnectionFactory(
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") int port) {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Long> longRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Integer> integerRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Double> doubleRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Double> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Float> floatRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Float> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Boolean> booleanRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Boolean> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Byte> byteRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Byte> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Character> characterRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Character> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // Order cache configurations
        cacheConfigurations.put("orders", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30)));
        cacheConfigurations.put("allOrders", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60)));
        cacheConfigurations.put("sortedOrders", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60)));
        cacheConfigurations.put("pagedOrders", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60)));
        cacheConfigurations.put("userOrder", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30)));
        cacheConfigurations.put("orderStats", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)));
        cacheConfigurations.put("ordersList", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30)));

        // Book cache configurations
        cacheConfigurations.put("books", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30)));
        cacheConfigurations.put("allBooks", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60)));
        cacheConfigurations.put("booksByTitle", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60)));
        cacheConfigurations.put("booksByTitles", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60)));
        cacheConfigurations.put("booksByFilter", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(2)));
        cacheConfigurations.put("booksByKeyword", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60)));
        cacheConfigurations.put("sortedBooks", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60)));
        cacheConfigurations.put("pagedBooks", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60)));
        cacheConfigurations.put("booksList", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30)));

        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(cacheConfigurations)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
                .build();
    }

    private Long generateRandomTtl() {
        return 60L + (long) (Math.random() * 540L);
    }
}
