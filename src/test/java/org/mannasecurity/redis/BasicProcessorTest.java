package org.mannasecurity.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mannasecurity.domain.TaskRequest;
import org.mannasecurity.redis.processors.CloneRequestProcessor;
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
    CloneRequestProcessor cloneRequestProcessor;

    //    @Autowired
//    ScanRequestListener scanRequestListener;
//
//    @Autowired
//    VerifyResultsListener verifyResultsListener;

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

        taskProcessorManager.setChannelProcessorMap(processorMap);

        taskProcessorManager.start();

        TaskRequest request = new TaskRequest();

        template.opsForList().leftPush(Channel.CLONE_REQUEST.toString(), request);

        Thread.sleep(3000);

        assertThat(cloneRequestProcessor.count()).isEqualTo(1);

//        ProjectMetadata pm = new ProjectMetadata();
//
//        template.send(Topic.CLONE_REQUEST.toString(), pm, bts("aaa"));
//        template.send(Topic.CLONE_REQUEST.toString(), pm, bts("bbb"));
//        template.send(Topic.CLONE_REQUEST.toString(), pm, bts("ccc"));
//        template.send(Topic.CLONE_REQUEST.toString(), pm, bts("ddd"));
//        template.send(Topic.CLONE_REQUEST.toString(), pm, bts("eee"));
//        template.send(Topic.CLONE_REQUEST.toString(), pm, bts("fff"));
//        template.send(Topic.CLONE_REQUEST.toString(), pm, bts("ggg"));
//
//        template.send(Topic.SCAN_REQUEST.toString(), pm, bts("hhh"));
//        template.send(Topic.SCAN_REQUEST.toString(), pm, bts("iii"));
//        template.send(Topic.SCAN_REQUEST.toString(), pm, bts("jjj"));
//        template.send(Topic.SCAN_REQUEST.toString(), pm, bts("kkk"));
//
//        template.send(Topic.VERIFY_RESULTS.toString(), pm, bts("lll"));
//        template.send(Topic.VERIFY_RESULTS.toString(), pm, bts("mmm"));
//        template.send(Topic.VERIFY_RESULTS.toString(), pm, bts("nnn"));
//
//        if(producerConfig.bytesProducerFactory() instanceof DefaultKafkaProducerFactory) {
//            ((DefaultKafkaProducerFactory)producerConfig.bytesProducerFactory()).destroy();
//        }
//
//        Thread.sleep(5000);
//
//        assertThat(cloneRequestListener.count()).isEqualTo(7);
//        assertThat(scanRequestListener.count()).isEqualTo(4);
//        assertThat(verifyResultsListener.count()).isEqualTo(3);
    }

}
