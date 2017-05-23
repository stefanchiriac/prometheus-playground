package ro.tremend.prometheus.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.MetricsServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Stefan Chiriac on 10.05.2017.
 * Configure and implement Metric endpoint
 */
@Configuration
@ConditionalOnClass(CollectorRegistry.class)
public class PrometheusConfiguration {

    @Bean
    @ConditionalOnMissingBean
    CollectorRegistry metricRegistry() {
        return CollectorRegistry.defaultRegistry;
    }

    @Bean
    ServletRegistrationBean registerPrometheusExporterServlet(CollectorRegistry metricRegistry) {
        return new ServletRegistrationBean(new MetricsServlet(metricRegistry), "/metrics");
    }
}