package org.mannasecurity.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jtmelton on 6/20/17.
 */
@Configuration
public class RedisSubscriberConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void receive(String message) {
        log.info("Received <" + message + ">");
    }
}
