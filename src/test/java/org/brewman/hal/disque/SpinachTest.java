package org.brewman.hal.disque;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.paluch.spinach.DisqueClient;
import biz.paluch.spinach.api.DisqueConnection;
import biz.paluch.spinach.api.Job;
import biz.paluch.spinach.api.sync.DisqueCommands;

public class SpinachTest {
    private static final Logger LOG = LoggerFactory
            .getLogger(SpinachTest.class);

    private static final String HOST = "disque://localhost:7711";
    private static final String QUEUE = "QUEUENAME";
    private static final String JOB = "JOBBODY";

    @BeforeClass
    public static void beforeClass() {

    }

    @Test
    public void testJedisqueSimple() throws URISyntaxException {
        DisqueClient client = new DisqueClient(HOST);
        DisqueConnection<String, String> connection = client.connect();
        DisqueCommands<String, String> sync = connection.sync();

        String jobId = sync.addjob(QUEUE, JOB, 1, TimeUnit.MINUTES);
        LOG.info("jobId: {}", jobId);

        List<Object> show = sync.show(jobId);
        for (Object o : show) {
            LOG.info("o: {} -> {}", o.getClass().getCanonicalName(), o);
        }

        List<Job<String, String>> jobs = sync.getjobs(QUEUE);

        for (Job<String, String> j : jobs) {
            StringBuilder sb = new StringBuilder();
            sb.append(j.getQueue() + ": ");
            sb.append(j.getId() + " -> ");
            sb.append(j.getBody());
            LOG.info("job: {}", sb);

            LOG.info("ackId: {}", sync.ackjob(j.getId()));
        }
    }

}
