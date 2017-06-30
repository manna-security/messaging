package org.mannasecurity.redis.processors;

import java.util.concurrent.atomic.AtomicInteger;
import org.mannasecurity.domain.TaskRequest;
import org.mannasecurity.processing.TaskProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by jtmelton on 6/21/17.
 */
@Component
public class BasicVerifyResultsProcessor implements TaskProcessor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private AtomicInteger count = new AtomicInteger();

    public Integer count() {
        return count.get();
    }

    @Override
    public void process(TaskRequest request) {
        log.info("Received request in verify results processor");

        count.incrementAndGet();
    }

}
