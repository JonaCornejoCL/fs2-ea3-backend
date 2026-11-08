package com.tipum.repository;

import com.tipum.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoria(String categoria);

    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stockCritico")
    List<Producto> findStockCritico();
}
