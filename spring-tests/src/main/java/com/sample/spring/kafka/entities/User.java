package com.sample.spring.kafka.entities;

import com.sample.spring.kafka.entities.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Type(type = "pg-uuid")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String firstName;
    private String lastName;

    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @Column(name = "role_id")
    private Integer roleId;
}
