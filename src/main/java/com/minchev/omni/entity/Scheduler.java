package com.minchev.omni.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "scheduler")
public class Scheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false, unique = true)
    private String key;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private int currentPage;

    public Scheduler() {
    }

    public Scheduler(String key, int currentPage) {
        this.key = key;
        this.currentPage = currentPage;
    }

    @PrePersist
    public void postPersist() {
       this.createdAt = LocalDateTime.now();
       this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
