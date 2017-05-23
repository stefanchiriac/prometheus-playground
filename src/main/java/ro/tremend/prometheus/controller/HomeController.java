package ro.tremend.prometheus.controller;

import ro.tremend.prometheus.config.CustomCollector;
import ro.tremend.prometheus.config.MetricHolder;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by Stefan Chiriac on 10.05.2017.
 */
@RestController
public class HomeController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/endpointA")
    public void handlerA() throws InterruptedException {
        logger.info("/endpointA");
        MetricHolder.increment("get", 1);
        Thread.sleep(RandomUtils.nextLong(0, 100));
    }

    @RequestMapping("/endpointB")
    public void handlerB() throws InterruptedException {
        logger.info("/endpointB");
        MetricHolder.increment("get", 1);
        Thread.sleep(RandomUtils.nextLong(0, 100));
    }


    @RequestMapping("/pushgateway")
    public void pushgateway() throws Exception {
        CollectorRegistry collectorRegistry = new CollectorRegistry();
        collectorRegistry.register(new CustomCollector());

        Gauge duration = Gauge.build()
                .name("my_batch_job_duration_seconds").help("Duration of my batch job in seconds.").register(collectorRegistry);
        Gauge.Timer durationTimer = duration.startTimer();
        try {
            logger.info("executing job");
            Thread.sleep(RandomUtils.nextLong(0, 100));
            Gauge lastSuccess = Gauge.build()
                    .name("my_batch_job_last_success").help("Last time my batch job succeeded, in unixtime.").register(collectorRegistry);
            lastSuccess.setToCurrentTime();
        } finally {
            durationTimer.setDuration();
            PushGateway pg = new PushGateway("localhost:9091");
            pg.pushAdd(collectorRegistry, "my_batch_job");
        }
    }

    @RequestMapping("/pushgateway/delete")
    public void pushgatewayDelete() throws IOException {
        PushGateway pg = new PushGateway("localhost:9091");
        pg.delete("my_batch_job");
    }
}
