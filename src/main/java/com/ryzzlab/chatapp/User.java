package com.ryzzlab.chatapp;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;
@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(unique = true,nullable = false)
    String email;
    @JsonIgnore
    String password;
    String displayName;
}
