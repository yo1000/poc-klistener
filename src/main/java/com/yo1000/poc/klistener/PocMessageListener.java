package com.yo1000.poc.klistener;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PocMessageListener {
    private final Logger logger = LoggerFactory.getLogger(PocMessageListener.class);
    private int counter = 1;

    private KafkaProperties kafkaProps;

    public PocMessageListener(KafkaProperties kafkaProps) {
        this.kafkaProps = kafkaProps;
    }

    /**
     * TODO: https://docs.spring.io/spring-kafka/docs/2.2.0.RC1/reference/html/_reference.html#stateful-retry
     * TODO: https://stackoverflow.com/questions/51831034/spring-kafka-how-to-retry-with-kafkalistener
     *
     * @param value
     * @param topic
     * @param groupId
     * @param offset
     * @throws InterruptedException
     */
    @KafkaListener(
            topics = {"${spring.kafka.template.default-topic}"}
    )
    public void listen(
            @Payload String value,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.GROUP_ID) String groupId,
            @Header(KafkaHeaders.OFFSET) int offset
    ) throws InterruptedException {
        logger.info("---- Start Listen ----");
        logger.info("Listen counter: {}", counter);
        logger.info("Value: {}", value);
        logger.info("Topic: {}", topic);
        logger.info("GroupId: {}", groupId);
        logger.info("Offset: {}", offset);

        if (counter++ % 5 == 0) {
            logger.info("<sleep 10sec>");
            Thread.sleep(10_000L);
        }

        logger.info("---- End Listen ----");
    }

    @Bean
    public NewTopic pocTopic(KafkaProperties props) {
        return TopicBuilder.name(props.getTemplate().getDefaultTopic()).build();
    }

    @Bean
    public NewTopic dltTopic(KafkaProperties props) {
        return TopicBuilder.name("DLT").build();
    }

}
