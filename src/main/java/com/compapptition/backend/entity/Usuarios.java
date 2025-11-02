package com.compapptition.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Usuarios {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private  String password;
    @Column(nullable = false)
    private String role;
}
