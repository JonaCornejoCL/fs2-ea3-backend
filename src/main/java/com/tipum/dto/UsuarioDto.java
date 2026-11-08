package com.tipum.dto;

import com.tipum.entity.Usuario;

import java.time.LocalDate;

public record UsuarioDto(
        Long id,
        String run,
        String nombre,
        String apellidos,
        String correo,
        String region,
        String comuna,
        String direccion,
        LocalDate fechaNacimiento,
        String rol,
        String rolEtiqueta
) {
    public static UsuarioDto from(Usuario u) {
        return new UsuarioDto(
                u.getId(),
                u.getRun(),
                u.getNombre(),
                u.getApellidos(),
                u.getCorreo(),
                u.getRegion(),
                u.getComuna(),
                u.getDireccion(),
                u.getFechaNacimiento(),
                u.getRol().name(),
                u.getRol().getEtiqueta()
        );
    }
}
