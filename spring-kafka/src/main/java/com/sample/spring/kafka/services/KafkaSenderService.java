package com.sample.spring.kafka.services;

import com.sample.spring.kafka.publisher.Event;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
@AllArgsConstructor
public class KafkaSenderService {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @EventListener
    public void publishProfileListener(Event<Object> event) {
        sendDataToKafka(event.getTopic(), event.getPayload());
    }

    public void sendDataToKafka(String topic, Object value) {
        log.debug("Send to kafka: topic= " + topic + ", value = " + value);
        kafkaTemplate.send(topic, value);
    }
}
