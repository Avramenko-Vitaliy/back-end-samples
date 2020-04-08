package com.sample.spring.security.entities.security;

import com.sample.spring.security.utils.TypeCreator;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {

    @Getter
    @AllArgsConstructor
    public enum Type implements TypeCreator<Role> {
        ADMIN(1),
        USER(2);

        private int id;

        @Override
        public Role getInstance() {
            return new Role(id, this);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "api_key", unique = true)
    private Type apiKey;
}
