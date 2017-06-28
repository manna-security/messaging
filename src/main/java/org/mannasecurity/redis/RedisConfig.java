package org.mannasecurity.redis;

import org.mannasecurity.domain.TaskRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by jtmelton on 6/20/17.
 */
@Configuration
public class RedisConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    public Jackson2JsonRedisSerializer<TaskRequest> taskRequestSerializer() {
        Jackson2JsonRedisSerializer<TaskRequest> serializer = new
            Jackson2JsonRedisSerializer<>(TaskRequest.class);
        return serializer;
    }

    @Bean
    public RedisSerializer<byte[]> byteSerializer() {
        RedisSerializer<byte[]> byteSerializer = new RedisSerializer<byte[]>() {
            @Override
            public byte[] serialize(byte[] bytes) throws SerializationException {
                return bytes;
            }

            @Override
            public byte[] deserialize(byte[] bytes) throws SerializationException {
                return bytes;
            }
        };

        return byteSerializer;
    }

    @Bean
    RedisTemplate<String, TaskRequest> template(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, TaskRequest> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(taskRequestSerializer());

        return template;
    }

}
