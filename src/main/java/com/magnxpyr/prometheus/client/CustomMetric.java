package com.magnxpyr.prometheus.client;

import io.prometheus.client.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gatz on 13.05.2017.
 */
public class CustomMetric extends SimpleCollector<CustomMetric.Child> implements Collector.Describable {

    public CustomMetric(Builder b) {
        super(b);
    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples.Sample> samples = new ArrayList<MetricFamilySamples.Sample>();
        for(Map.Entry<List<String>, CustomMetric.Child> c: children.entrySet()) {
            samples.add(new MetricFamilySamples.Sample(fullname, labelNames, c.getKey(), c.getValue().get()));
        }
        MetricFamilySamples mfs = new MetricFamilySamples(fullname, Type.UNTYPED, help, samples);

        List<MetricFamilySamples> mfsList = new ArrayList<MetricFamilySamples>();
        mfsList.add(mfs);
        return mfsList;
    }

    @Override
    public List<MetricFamilySamples> describe() {
        List<MetricFamilySamples> mfsList = new ArrayList<MetricFamilySamples>();
        mfsList.add(new CounterMetricFamily(fullname, help, labelNames));
        return mfsList;
    }

    @Override
    protected Child newChild() {
        return new Child();
    }

    public static class Child {
        private final DoubleAdder value = new DoubleAdder();
        /**
         * Increment the counter by 1.
         */
        public void inc() {
            inc(1);
        }
        /**
         * Increment the counter by the given amount.
         * @throws IllegalArgumentException If amt is negative.
         */
        public void inc(double amt) {
            if (amt < 0) {
                throw new IllegalArgumentException("Amount to increment must be non-negative.");
            }
            value.add(amt);
        }
        /**
         * Get the value of the counter.
         */
        public double get() {
            return value.sum();
        }
    }
}
