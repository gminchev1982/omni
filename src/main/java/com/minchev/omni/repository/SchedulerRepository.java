package com.minchev.omni.repository;

import com.minchev.omni.entity.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

    Optional<Scheduler> findByKey(String key);
}
