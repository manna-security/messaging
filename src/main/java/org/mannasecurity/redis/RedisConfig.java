package org.mannasecurity.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mannasecurity.domain.TaskRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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
        serializer.setObjectMapper(serializingObjectMapper());
        return serializer;
    }

    @Bean
    @Primary
    public ObjectMapper serializingObjectMapper() {
        return Jackson2ObjectMapperBuilder.json()
            .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
            .modules(new JavaTimeModule())
            .build();
    }

    @Bean
    RedisTemplate<String, TaskRequest> template(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, TaskRequest> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        RedisSerializer<TaskRequest> requestSerializer = taskRequestSerializer();

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(requestSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(requestSerializer);

        template.setEnableDefaultSerializer(false);

        return template;
    }

}
