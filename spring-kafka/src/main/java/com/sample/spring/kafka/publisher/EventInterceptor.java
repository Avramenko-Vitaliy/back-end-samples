package com.sample.spring.kafka.publisher;

import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class EventInterceptor {

    private final ApplicationEventPublisher publisher;

    @AfterReturning(pointcut = "@annotation(event)", returning = "retVal")
    public void publishEvent(PublishToKafka event, Object retVal) {
        publisher.publishEvent(new Event<>(event.value(), retVal));
    }
}
