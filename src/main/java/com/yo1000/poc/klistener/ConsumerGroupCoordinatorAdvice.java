package com.yo1000.poc.klistener;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ConsumerGroupCoordinatorAdvice {
    public ConsumerGroupCoordinatorAdvice(KafkaProperties kafkaProps) {
        final String maxPollIntervalMsAsString = kafkaProps.getConsumer().getProperties().get("max.poll.interval.ms");

        if (maxPollIntervalMsAsString.matches("^[0-9_]+$")) {
            HealthyContainer.newInstance(Long.parseLong(maxPollIntervalMsAsString));
        } else {
            HealthyContainer.newInstance();
        }
    }

    @Around("execution(* com.yo1000.poc.klistener.MessageListener+.listen(..))")
    public Object updateHealthy(ProceedingJoinPoint joinPoint) throws Throwable {
        HealthyContainer.getInstance().setTimeout();

        Object retVal = joinPoint.proceed();

        HealthyContainer.getInstance().healthy();

        return retVal;
    }
}
