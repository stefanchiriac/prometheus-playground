package ro.tremend.prometheus.config;

import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Stefan Chiriac on 13.05.2017.
 */
@Component
public class CustomCollector extends Collector {

    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<>();
        // With no labels.
        mfs.add(new GaugeMetricFamily("my_gauge", "help", 42));
        // With labels
        GaugeMetricFamily labeledGauge = new GaugeMetricFamily("my_other_gauge", "help", Arrays.asList("labelname"));
        labeledGauge.addMetric(Arrays.asList("foo"), 4);
        labeledGauge.addMetric(Arrays.asList("bar"), 5);
        mfs.add(labeledGauge);
        return mfs;
    }
}
