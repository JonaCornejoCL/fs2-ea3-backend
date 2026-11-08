package com.tipum.dto;

import com.tipum.entity.DetalleOrden;
import com.tipum.entity.Orden;

import java.time.LocalDateTime;
import java.util.List;

public record OrdenDto(
        Long id,
        String codigo,
        Long usuarioId,
        String cliente,
        String correo,
        LocalDateTime fecha,
        Integer monto,
        String estado,
        String direccion,
        List<DetalleOrdenDto> items
) {
    public static OrdenDto from(Orden o) {
        String cliente = o.getUsuario() != null
                ? o.getUsuario().getNombre() + " " + o.getUsuario().getApellidos()
                : "Invitado";
        String correo = o.getUsuario() != null ? o.getUsuario().getCorreo() : "";
        Long usuarioId = o.getUsuario() != null ? o.getUsuario().getId() : null;

        List<DetalleOrdenDto> items = o.getItems().stream()
                .map(OrdenDto::toItemDto)
                .toList();

        return new OrdenDto(
                o.getId(),
                o.getCodigo(),
                usuarioId,
                cliente,
                correo,
                o.getFecha(),
                o.getMonto(),
                o.getEstado(),
                o.getDireccion(),
                items
        );
    }

    private static DetalleOrdenDto toItemDto(DetalleOrden d) {
        return new DetalleOrdenDto(d.getId(), d.getNombre(), d.getCantidad(), d.getPrecio());
    }
}
