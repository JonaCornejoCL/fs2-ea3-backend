package com.tipum.repository;

import com.tipum.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    List<Orden> findByUsuarioCorreoOrderByFechaDesc(String correo);
}
