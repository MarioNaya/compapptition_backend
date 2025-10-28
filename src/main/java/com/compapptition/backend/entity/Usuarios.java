package com.compapptition.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String userName;
    private  String password;
    private String role;
}
