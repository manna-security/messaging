package org.mannasecurity.redis;

import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jtmelton on 6/21/17.
 */
public class RedisSubscriber {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void receive(byte[] message) {
        log.info("Received <" + new String(message, StandardCharsets.UTF_8) + ">");
    }

}
