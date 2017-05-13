package com.magnxpyr.prometheus.controller;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by gatz on 10.05.2017.
 */
@RestController
public class HomeController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CollectorRegistry collectorRegistry;

    @RequestMapping("/endpointA")
    public void handlerA() throws InterruptedException {
        logger.info("/endpointA");
        Thread.sleep(RandomUtils.nextLong(0, 100));
    }

    @RequestMapping("/endpointB")
    public void handlerB() throws InterruptedException {
        logger.info("/endpointB");
        Thread.sleep(RandomUtils.nextLong(0, 100));
    }


    @RequestMapping("/pushgateway")
    public void pushgateway() throws IOException{
        Gauge duration = Gauge.build()
                .name("my_batch_job_duration_seconds").help("Duration of my batch job in seconds.").register(collectorRegistry);
        Gauge.Timer durationTimer = duration.startTimer();
        try {
            // Your code here.

            // This is only added to the registry after success,
            // so that a previous success in the Pushgateway isn't overwritten on failure.
            Gauge lastSuccess = Gauge.build()
                    .name("my_batch_job_last_success").help("Last time my batch job succeeded, in unixtime.").register(collectorRegistry);
            lastSuccess.setToCurrentTime();
        } finally {
            durationTimer.setDuration();
            PushGateway pg = new PushGateway("127.0.0.1:9091");
            pg.pushAdd(collectorRegistry, "my_batch_job");
        }
    }
}
