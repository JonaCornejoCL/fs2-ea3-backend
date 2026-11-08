package com.tipum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String run;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(nullable = false)
    private String password;

    @Column(length = 150)
    private String region;

    @Column(length = 150)
    private String comuna;

    @Column(length = 300)
    private String direccion;

    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Rol rol;
}
