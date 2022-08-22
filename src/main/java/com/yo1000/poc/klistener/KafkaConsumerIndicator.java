package com.yo1000.poc.klistener;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerIndicator extends AbstractHealthIndicator {
    public static final HealthyContainer healthyContainer = HealthyContainer.getInstance();

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        if (healthyContainer.isHealthy()) {
            builder.up();
        } else {
            builder.down();
        }
    }
}
