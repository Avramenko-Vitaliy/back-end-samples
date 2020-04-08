package com.sample.spring.db.repositories;

import com.sample.spring.db.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
}
