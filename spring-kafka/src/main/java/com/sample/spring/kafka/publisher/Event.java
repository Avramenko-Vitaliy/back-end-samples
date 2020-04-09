package com.sample.spring.kafka.publisher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event<T> {

    private String topic;

    private T payload;
}
