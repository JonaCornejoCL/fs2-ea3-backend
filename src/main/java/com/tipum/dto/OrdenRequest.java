package com.tipum.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrdenRequest(
        @NotEmpty(message = "La orden debe incluir al menos un producto")
        @Valid
        List<DetalleOrdenRequest> items,

        String direccion
) {
}
