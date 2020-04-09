package com.sample.spring.kafka.logs.repositories;

import com.sample.spring.kafka.logs.entities.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsRepository extends JpaRepository<UserLog, Integer> {
}
