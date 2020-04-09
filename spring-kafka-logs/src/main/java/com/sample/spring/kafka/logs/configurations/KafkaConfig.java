package com.sample.spring.kafka.logs.configurations;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.BatchLoggingErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Objects;

@CommonsLog
@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaSingleListenerContainerFactory(
            ConsumerFactory<Object, Object> consumerFactory,
            KafkaTemplate<Object, Object> kafkaTemplate,
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer) {

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, consumerFactory);
        FixedBackOff backOff = new FixedBackOff();
        backOff.setMaxAttempts(1);
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate), backOff));
        factory.setMessageConverter(new StringJsonMessageConverter());
        return factory;
    }

    @Bean
    public KafkaListenerErrorHandler kafkaListenerErrorHandler() {
        return (message, e) -> {
            log.error(String.format("Listener method thrown an exception. Message is %s", message), e);
            return null;
        };
    }
}
