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
public class RedisPublisherConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

//    @Bean
//    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
//                                            MessageListenerAdapter listenerAdapter) {
//
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
//
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
//        return new MessageListenerAdapter(subscriber, "receive");
//    }
//
//    @Bean
//    RedisSubscriber subscriber() {
//        return new RedisSubscriber();
//    }

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

//    public static void main(String[] args) throws InterruptedException {
//
//        ApplicationContext ctx = SpringApplication.run(RedisPublisherConfig.class, args);
//
//        StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
//        CountDownLatch latch = ctx.getBean(CountDownLatch.class);
//
////        LOGGER.info("Sending message...");
////        template.opsForList().po
//        template.convertAndSend("chat", "Hello from Redis!");
//
//        latch.await();
//
//        System.exit(0);
//    }
}
