package com.tipum.controller;

import com.tipum.dto.EstadoRequest;
import com.tipum.dto.OrdenDto;
import com.tipum.dto.OrdenRequest;
import com.tipum.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@Tag(name = "Órdenes", description = "Gestión de órdenes y boletas (ADMIN y VENDEDOR pueden ver; CLIENTE compra)")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @Operation(summary = "Listar todas las órdenes")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public List<OrdenDto> listar() {
        return ordenService.listar();
    }

    @Operation(summary = "Listar las órdenes del usuario autenticado")
    @GetMapping("/mis-ordenes")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public List<OrdenDto> misOrdenes(Authentication authentication) {
        return ordenService.listarPorUsuario(authentication.getName());
    }

    @Operation(summary = "Obtener una orden por id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public OrdenDto obtener(@PathVariable Long id) {
        return ordenService.obtener(id);
    }

    @Operation(summary = "Crear una orden (compra/checkout)")
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public ResponseEntity<OrdenDto> crear(Authentication authentication,
                                          @Valid @RequestBody OrdenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordenService.crear(authentication.getName(), request));
    }

    @Operation(summary = "Actualizar el estado de una orden")
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public OrdenDto actualizarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest request) {
        return ordenService.actualizarEstado(id, request.estado());
    }
}
