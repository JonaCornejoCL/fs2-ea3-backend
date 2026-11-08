package com.tipum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "La categoría es obligatoria")
        String categoria,

        @NotNull(message = "El precio es obligatorio")
        @Min(value = 0, message = "El precio debe ser 0 o mayor")
        Integer precio,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock debe ser 0 o mayor")
        Integer stock,

        @NotNull(message = "El stock crítico es obligatorio")
        @Min(value = 0, message = "El stock crítico debe ser 0 o mayor")
        Integer stockCritico,

        Boolean enOferta,
        @Min(value = 0, message = "El precio de oferta debe ser 0 o mayor")
        Integer precioOferta,
        String imagen,
        String descripcion
) {
}
