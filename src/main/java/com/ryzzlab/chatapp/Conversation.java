package com.ryzzlab.chatapp;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversations")
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(name = "is_group")
    boolean group;
    String name;
    @CreationTimestamp
    LocalDateTime createdAt;


}
