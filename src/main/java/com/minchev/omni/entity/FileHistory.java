package com.minchev.omni.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@Entity
@Table(name = "file_history")
public class FileHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String name;

    @Column( nullable = false)
    private String nameOriginal;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

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
