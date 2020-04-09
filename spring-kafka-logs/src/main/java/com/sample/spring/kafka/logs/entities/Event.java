package com.sample.spring.kafka.logs.entities;

import com.sample.spring.kafka.logs.utils.TypeCreator;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class Event {

    @Getter
    @AllArgsConstructor
    public enum Type implements TypeCreator<Event> {
        REGISTER(1);

        private int id;

        @Override
        public Event getInstance() {
            return new Event(id, this);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "api_key", unique = true)
    private Type apiKey;
}
