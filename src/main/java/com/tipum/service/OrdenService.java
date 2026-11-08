package com.tipum.service;

import com.tipum.dto.DetalleOrdenRequest;
import com.tipum.dto.OrdenDto;
import com.tipum.dto.OrdenRequest;
import com.tipum.entity.DetalleOrden;
import com.tipum.entity.Orden;
import com.tipum.entity.Producto;
import com.tipum.entity.Usuario;
import com.tipum.exception.ResourceNotFoundException;
import com.tipum.repository.OrdenRepository;
import com.tipum.repository.ProductoRepository;
import com.tipum.repository.UsuarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdenService {

    private static final List<String> ESTADOS_VALIDOS = List.of("Pendiente", "Completado", "Rechazado");

    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public OrdenService(OrdenRepository ordenRepository,
                        ProductoRepository productoRepository,
                        UsuarioRepository usuarioRepository) {
        this.ordenRepository = ordenRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<OrdenDto> listar() {
        return ordenRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha"))
                .stream().map(OrdenDto::from).toList();
    }

    @Transactional(readOnly = true)
    public OrdenDto obtener(Long id) {
        return OrdenDto.from(buscar(id));
    }

    @Transactional(readOnly = true)
    public List<OrdenDto> listarPorUsuario(String correo) {
        return ordenRepository.findByUsuarioCorreoOrderByFechaDesc(correo)
                .stream().map(OrdenDto::from).toList();
    }

    @Transactional
    public OrdenDto crear(String correoUsuario, OrdenRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + correoUsuario));

        Orden orden = Orden.builder()
                .codigo("ORDER" + System.currentTimeMillis())
                .usuario(usuario)
                .fecha(LocalDateTime.now())
                .estado("Pendiente")
                .direccion(request.direccion())
                .monto(0)
                .build();

        int montoTotal = 0;
        for (DetalleOrdenRequest itemReq : request.items()) {
            Producto producto = productoRepository.findById(itemReq.productoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + itemReq.productoId()));

            if (producto.getStock() < itemReq.cantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            int precio = (producto.getEnOferta() != null && producto.getEnOferta()
                    && producto.getPrecioOferta() != null)
                    ? producto.getPrecioOferta()
                    : producto.getPrecio();

            int subtotal = precio * itemReq.cantidad();
            montoTotal += subtotal;

            producto.setStock(producto.getStock() - itemReq.cantidad());
            productoRepository.save(producto);

            DetalleOrden detalle = DetalleOrden.builder()
                    .nombre(producto.getNombre())
                    .cantidad(itemReq.cantidad())
                    .precio(precio)
                    .build();
            orden.agregarItem(detalle);
        }

        orden.setMonto(montoTotal);
        return OrdenDto.from(ordenRepository.save(orden));
    }

    @Transactional
    public OrdenDto actualizarEstado(Long id, String estado) {
        Orden orden = buscar(id);
        String estadoLimpio = estado == null ? "" : estado.trim();

        if (!ESTADOS_VALIDOS.contains(estadoLimpio)) {
            throw new IllegalArgumentException("Estado inválido. Valores permitidos: Pendiente, Completado, Rechazado");
        }

        orden.setEstado(estadoLimpio);
        return OrdenDto.from(ordenRepository.save(orden));
    }

    private Orden buscar(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada: " + id));
    }
}
