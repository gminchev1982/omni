package com.minchev.omni.repository;

import com.minchev.omni.entity.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

    Optional<Scheduler> findByKey(String key);
}
