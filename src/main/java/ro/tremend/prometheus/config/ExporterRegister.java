package ro.tremend.prometheus.config;

import io.prometheus.client.Collector;
import io.prometheus.client.hotspot.MemoryPoolsExports;
import io.prometheus.client.hotspot.StandardExports;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan Chiriac on 10.05.2017.
 * Metric exporter register bean to register a list of exporters to the default registry
 */
public class ExporterRegister {
    private List<Collector> collectors;

    ExporterRegister(List<Collector> collectors) {
        for (Collector collector : collectors) {
            collector.register();
        }
        this.collectors = collectors;
    }

    public List<Collector> getCollectors() {
        return collectors;
    }

    @Bean
    ExporterRegister exporterRegister() {
        List<Collector> collectors = new ArrayList<>();
        collectors.add(new StandardExports());
        collectors.add(new MemoryPoolsExports());
        return new ExporterRegister(collectors);
    }
}
