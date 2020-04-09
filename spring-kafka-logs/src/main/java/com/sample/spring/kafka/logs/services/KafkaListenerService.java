package com.sample.spring.kafka.logs.services;

import com.sample.spring.kafka.logs.configurations.KafkaTopics;
import com.sample.spring.kafka.logs.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
@AllArgsConstructor
public class KafkaListenerService {

    private final LogsService logsService;

    @KafkaListener(topics = KafkaTopics.LOGS_USER_REGISTERED, containerFactory = "kafkaSingleListenerContainerFactory")
    public void logsListener(UserDto dto) {
        logsService.writeLog(dto);
    }
}
