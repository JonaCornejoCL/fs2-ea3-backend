package com.tipum.dto;

public record DetalleOrdenDto(
        Long id,
        String nombre,
        Integer cantidad,
        Integer precio
) {
}
