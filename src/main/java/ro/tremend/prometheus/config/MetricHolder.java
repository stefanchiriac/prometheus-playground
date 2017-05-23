package ro.tremend.prometheus.config;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;

/**
 * Created by Stefan Chiriac on 14.05.2017.
 */
public class MetricHolder {
    private static final Counter requestsTotal = Counter.build()
            .name("custom_requests_total")
            .labelNames("method", "status")
            .help("The number of requests")
            .register();

    private static final Summary requestDurationSeconds = Summary.build()
            .name("custom_request_duration_seconds")
            .labelNames("method", "duration")
            .help("The duration for each request")
            .register();

    public static void increment(String method, Integer status){
        requestsTotal.labels(method, status.toString()).inc();
    }

    public static void requestSummary(String type, Double duration){
        requestDurationSeconds.labels(type, duration.toString()).observe(duration/1000);
    }
}