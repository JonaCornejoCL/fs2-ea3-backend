package com.tipum.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private Integer monto;

    /** Valores permitidos: "Pendiente", "Completado", "Rechazado". */
    @Column(nullable = false, length = 30)
    private String estado;

    @Column(length = 300)
    private String direccion;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleOrden> items = new ArrayList<>();

    public void agregarItem(DetalleOrden item) {
        item.setOrden(this);
        this.items.add(item);
    }
}
