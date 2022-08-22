package com.yo1000.poc.klistener;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    private final Logger logger = LoggerFactory.getLogger(MessageListener.class);
    private int counter = 1;

    public MessageListener(
            KafkaProperties kafkaProps,
            ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory
    ) {
        kafkaListenerContainerFactory.setErrorHandler((e, consumerRecord) -> HealthyContainer.getInstance().healthy());
    }

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
}
