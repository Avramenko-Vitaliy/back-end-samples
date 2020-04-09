package com.sample.spring.kafka.logs.services;

import com.sample.spring.kafka.logs.dtos.UserDto;
import com.sample.spring.kafka.logs.entities.Event;
import com.sample.spring.kafka.logs.entities.UserLog;
import com.sample.spring.kafka.logs.repositories.LogsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogsService {

    private final LogsRepository logsRepository;

    public void writeLog(UserDto dto) {
        UserLog log = UserLog.builder()
                .userId(dto.getId())
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .event(Event.Type.REGISTER.getInstance())
                .build();
        logsRepository.save(log);
    }
}
