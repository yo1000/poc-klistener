package com.yo1000.poc.klistener;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PocMessageListener {
    private static int counter = 0;

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
        if (counter++ == 5) {
            System.out.println("sleep 300sec");
            Thread.sleep(300_000L);
        }

        System.out.println(value);
    }

    @Bean
    public NewTopic pocTopic(KafkaProperties props) {
        return TopicBuilder.name(props.getTemplate().getDefaultTopic()).build();
    }
}
