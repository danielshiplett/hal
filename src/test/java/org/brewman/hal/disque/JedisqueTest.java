package org.brewman.hal.disque;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.xetorthio.jedisque.Jedisque;
import com.github.xetorthio.jedisque.Job;

public class JedisqueTest {
    private static final Logger LOG = LoggerFactory
            .getLogger(JedisqueTest.class);

    private static final String HOST1 = "disque://localhost:7711";
    private static final String HOST2 = "disque://localhost:7712";
    private static final String HOST3 = "disque://localhost:7713";

    private static final String[] HOSTS = new String[] { HOST1, HOST2, HOST3 };

    private static final String QUEUE = "QUEUENAME";
    private static final String JOB = "JOBBODY";
    private static final Long TIMEOUT = 10000l;

    private static URI[] hosts = null;

    @BeforeClass
    public static void beforeClass() throws URISyntaxException {
        List<URI> uris = new ArrayList<URI>();

        for (String h : HOSTS) {
            uris.add(new URI(h));
        }

        hosts = uris.toArray(new URI[] {});
    }

    @Test
    public void testJedisqueSimple() throws URISyntaxException {
        try (Jedisque q = new Jedisque(hosts)) {
            String jobId = q.addJob(QUEUE, JOB, TIMEOUT);
            LOG.info("jobId: {}", jobId);

            // JobInfo jobInfo = q.show(jobId);
            // LOG.info("jobInfo: {}", jobInfo);

            List<Job> jobs = q.getJob(0, Long.MAX_VALUE, QUEUE);

            for (Job j : jobs) {
                StringBuilder sb = new StringBuilder();
                sb.append(j.getQueueName() + ": ");
                sb.append(j.getId() + " -> ");
                sb.append(j.getBody());
                LOG.info("job: {}", sb);

                Long ackId = q.ackjob(j.getId());
                LOG.info("ackId: {}", ackId);

            }
        }
    }
}
