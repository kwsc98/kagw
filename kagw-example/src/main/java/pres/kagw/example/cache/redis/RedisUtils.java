package pres.kagw.example.cache.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author kwsc98
 */
public class RedisUtils {


    public static <K, V> RedisTemplate<K, V> redisCacheTemplate(String redisHost, int redisPort, String redisPassword, int redisDatabase) {
        RedisTemplate<K, V> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(initLettuceConnectionFactory(redisHost, redisPort, redisPassword, redisDatabase));
        return template;
    }


    public static LettuceConnectionFactory initLettuceConnectionFactory(String redisHost, int redisPort, String redisPassword, int redisDatabase) {
        //redis配置
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        redisConfiguration.setDatabase(redisDatabase);
        redisConfiguration.setPassword(RedisPassword.of(redisPassword));
        //连接池配置
        GenericObjectPoolConfig<?> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxIdle(16);
        genericObjectPoolConfig.setMinIdle(4);
        genericObjectPoolConfig.setMaxTotal(32);
        //redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofMillis(10000));
        builder.shutdownTimeout(Duration.ofMillis(4000));
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();
        //根据配置和客户端配置创建连接
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration, lettuceClientConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

}
