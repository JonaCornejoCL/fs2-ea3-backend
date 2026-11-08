package com.tipum.entity;

public enum Rol {
    ADMIN("Administrador"),
    VENDEDOR("Vendedor"),
    CLIENTE("Cliente");

    private final String etiqueta;

    Rol(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
