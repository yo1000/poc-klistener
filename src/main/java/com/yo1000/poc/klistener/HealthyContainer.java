package com.yo1000.poc.klistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HealthyContainer {
    private static final String RANDOM_ID = UUID.randomUUID().toString();

    private static final long DEFAULT_HEALTHY_ALLOWED = 300_000L;

    private static final Map<String, HealthyContainer> caches = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(HealthyContainer.class);

    private final long healthyAllowedMillis;

    private long healthyBaseline;

    private HealthyContainer(long healthyAllowedMillis) {
        this.healthyAllowedMillis = healthyAllowedMillis;
        healthy();
    }

    public boolean isHealthy() {
        final long current = System.currentTimeMillis();
        final boolean healthy = current < healthyBaseline + healthyAllowedMillis;
        logger.debug("Healthy status => {} (current: {}, baseline: {} allowed: {})", healthy, current, healthyBaseline, healthyAllowedMillis);
        return healthy;
    }

    public synchronized boolean setTimeout() {
        updateBaseline(System.currentTimeMillis());
        logger.debug("Healthy baseline was updated => {}", healthyBaseline);
        return isHealthy();
    }

    public synchronized boolean healthy() {
        healthyBaseline = Long.MAX_VALUE - healthyAllowedMillis;
        logger.debug("Healthy baseline was updated => {}", healthyBaseline);
        return isHealthy();
    }

    public synchronized boolean unhealthy() {
        healthyBaseline = -healthyAllowedMillis;
        logger.debug("Healthy baseline was updated => {}", healthyBaseline);
        return isHealthy();
    }

    private synchronized void updateBaseline(long baseline) {
        healthyBaseline = baseline;
    }

    public static HealthyContainer getInstance() {
        return getInstance(RANDOM_ID);
    }

    public static HealthyContainer getInstance(String id) {
        return caches.get(id);
    }

    public static HealthyContainer newInstance() {
        return newInstance(DEFAULT_HEALTHY_ALLOWED);
    }

    public static HealthyContainer newInstance(long healthyAllowedMillis) {
        return newInstance(RANDOM_ID, healthyAllowedMillis);
    }

    public static HealthyContainer newInstance(String id) {
        return newInstance(id, DEFAULT_HEALTHY_ALLOWED);
    }

    public static HealthyContainer newInstance(String id, long healthyAllowedMillis) {
        return caches.put(id, new HealthyContainer(healthyAllowedMillis));
    }
}
