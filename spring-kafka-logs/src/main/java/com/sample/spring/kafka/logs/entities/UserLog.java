package com.sample.spring.kafka.logs.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_logs")
public class UserLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_logs_id_seq")
    @SequenceGenerator(name = "user_logs_id_seq", sequenceName = "user_logs_id_seq", allocationSize = 1)
    private Integer id;

    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @CreationTimestamp
    private LocalDateTime creationDate;
}
