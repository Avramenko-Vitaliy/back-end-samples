package com.sample.spring.kafka.configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaTopics {

    public static final String LOGS_USER_REGISTERED = "logs.user.registered";

    @Bean
    public NewTopic logsUserRegistered() {
        return new NewTopic(LOGS_USER_REGISTERED, 1, (short) 1);
    }
}
