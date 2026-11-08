package com.tipum.dto;

public record LoginResponse(
        String token,
        String tipo,
        UsuarioDto usuario
) {
    public static LoginResponse of(String token, UsuarioDto usuario) {
        return new LoginResponse(token, "Bearer", usuario);
    }
}
