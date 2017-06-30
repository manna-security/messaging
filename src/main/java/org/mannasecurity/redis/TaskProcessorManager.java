package org.mannasecurity.redis;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.mannasecurity.domain.TaskRequest;
import org.mannasecurity.processing.TaskProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by jtmelton on 6/21/17.
 */
@Component
public class TaskProcessorManager {

    private final Logger mainlog = LoggerFactory.getLogger(this.getClass());

    private boolean running = true;

    @Autowired
    RedisTemplate<String, TaskRequest> template;

    private Map<String, TaskProcessor> channelProcessorMap;

    public TaskProcessorManager() {}

    @Autowired
    public TaskProcessorManager(final Map<String, TaskProcessor> channelProcessorMap) {
        this.channelProcessorMap = Collections.unmodifiableMap(channelProcessorMap);
    }

    public void setChannelProcessorMap(final Map<String, TaskProcessor> channelProcessorMap) {
        this.channelProcessorMap = Collections.unmodifiableMap(channelProcessorMap);
    }

    // start all processors
    public void start() {
        for (String channel : channelProcessorMap.keySet()) {
            Thread thread = new Thread(
                new TaskProcessorRunner(channel, channelProcessorMap.get(channel)));
            thread.setName("RedisProcessor-" + channel + "-Thread");
            thread.start();

            mainlog.debug("Started processor thread '{}'", thread.getName());
        }
    }

    public void stop() {
        this.running = false;
    }

    class TaskProcessorRunner implements Runnable {

        private final Logger innerlog = LoggerFactory.getLogger(this.getClass());

        private final String channel;
        private final TaskProcessor processor;

        public TaskProcessorRunner(final String channel, final TaskProcessor processor) {
            this.channel = channel;
            this.processor = processor;
        }

        @Override
        public void run() {
            innerlog.debug("Processor Listening on '{}'", channel);
            BoundListOperations<String, TaskRequest> operations = template.boundListOps(channel);

            while (running) {
                try {
                    TaskRequest request = operations.rightPop(30, TimeUnit.SECONDS);

                    if (request != null) {
                        innerlog.debug(
                            "Received TaskRequest to process on channel '{}' and sending to "
                            + "processor '{}'", channel, processor.getClass());

                        processor.process(request);
                    }
                } catch (RuntimeException e) {
                    innerlog.debug("Redis error found.", e);
                }
            }
        }
    }
}
