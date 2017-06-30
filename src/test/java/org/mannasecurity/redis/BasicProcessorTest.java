package org.mannasecurity.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mannasecurity.domain.TaskRequest;
import org.mannasecurity.processing.TaskProcessor;
import org.mannasecurity.redis.processors.BasicCloneRequestProcessor;
import org.mannasecurity.redis.processors.BasicScanRequestProcessor;
import org.mannasecurity.redis.processors.BasicVerifyResultsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

/**
 * Created by jtmelton on 6/21/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicProcessorTest {

    static RedisServer redisServer;

    @Autowired
    BasicCloneRequestProcessor cloneRequestProcessor;

    @Autowired
    BasicScanRequestProcessor scanRequestProcessor;

    @Autowired
    BasicVerifyResultsProcessor verifyResultsProcessor;

    @Autowired
    RedisTemplate<String, TaskRequest> template;

    @Autowired
    TaskProcessorManager taskProcessorManager;

    @BeforeClass
    public static void setUp() throws Exception {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterClass
    public static void tearDown() {
        redisServer.stop();
    }

    @Test
    public void testTemplate() throws Exception {

        Map<String, TaskProcessor> processorMap = new HashMap<>();
        processorMap.put(Channel.CLONE_REQUEST.toString(), cloneRequestProcessor);
        processorMap.put(Channel.SCAN_REQUEST.toString(), scanRequestProcessor);
        processorMap.put(Channel.VERIFY_RESULTS.toString(), verifyResultsProcessor);

        taskProcessorManager.setChannelProcessorMap(processorMap);

        taskProcessorManager.start();

        template.opsForList().leftPush(Channel.CLONE_REQUEST.toString(), new TaskRequest());
        template.opsForList().leftPush(Channel.CLONE_REQUEST.toString(), new TaskRequest());
        template.opsForList().leftPush(Channel.CLONE_REQUEST.toString(), new TaskRequest());
        template.opsForList().leftPush(Channel.CLONE_REQUEST.toString(), new TaskRequest());
        template.opsForList().leftPush(Channel.CLONE_REQUEST.toString(), new TaskRequest());
        template.opsForList().leftPush(Channel.CLONE_REQUEST.toString(), new TaskRequest());
        template.opsForList().leftPush(Channel.CLONE_REQUEST.toString(), new TaskRequest());

        template.opsForList().leftPush(Channel.SCAN_REQUEST.toString(), new TaskRequest());
        template.opsForList().leftPush(Channel.SCAN_REQUEST.toString(), new TaskRequest());
        template.opsForList().leftPush(Channel.SCAN_REQUEST.toString(), new TaskRequest());
        template.opsForList().leftPush(Channel.SCAN_REQUEST.toString(), new TaskRequest());

        template.opsForList().leftPush(Channel.VERIFY_RESULTS.toString(), new TaskRequest());
        template.opsForList().leftPush(Channel.VERIFY_RESULTS.toString(), new TaskRequest());
        template.opsForList().leftPush(Channel.VERIFY_RESULTS.toString(), new TaskRequest());

        Thread.sleep(3000);

        assertThat(cloneRequestProcessor.count()).isEqualTo(7);
        assertThat(scanRequestProcessor.count()).isEqualTo(4);
        assertThat(verifyResultsProcessor.count()).isEqualTo(3);
    }

}
