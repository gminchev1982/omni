package com.minchev.omni.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String name;

    @Column( nullable = false)
    private String code;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="file_history_id", nullable=false)
    private FileHistory fileHistory;

    @PrePersist
    public void postPersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
