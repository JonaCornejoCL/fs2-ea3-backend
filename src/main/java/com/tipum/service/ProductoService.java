package com.tipum.service;

import com.tipum.dto.ProductoRequest;
import com.tipum.entity.Producto;
import com.tipum.exception.ResourceNotFoundException;
import com.tipum.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ProductoService {

    private static final Set<String> CATEGORIAS_VALIDAS = Set.of("cafe", "pasteleria", "merchandising");

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Producto obtener(Long id) {
        return buscar(id);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarStockCritico() {
        return productoRepository.findStockCritico();
    }

    @Transactional
    public Producto crear(ProductoRequest req) {
        Producto producto = Producto.builder()
                .nombre(req.nombre())
                .categoria(validarCategoria(req.categoria()))
                .precio(req.precio())
                .stock(req.stock())
                .stockCritico(req.stockCritico())
                .enOferta(req.enOferta() != null && req.enOferta())
                .precioOferta(req.precioOferta())
                .imagen(req.imagen())
                .descripcion(req.descripcion())
                .build();
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizar(Long id, ProductoRequest req) {
        Producto producto = buscar(id);
        producto.setNombre(req.nombre());
        producto.setCategoria(validarCategoria(req.categoria()));
        producto.setPrecio(req.precio());
        producto.setStock(req.stock());
        producto.setStockCritico(req.stockCritico());
        producto.setEnOferta(req.enOferta() != null && req.enOferta());
        producto.setPrecioOferta(req.precioOferta());
        producto.setImagen(req.imagen());
        producto.setDescripcion(req.descripcion());
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        productoRepository.delete(buscar(id));
    }

    private Producto buscar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));
    }

    private String validarCategoria(String categoria) {
        String cat = categoria == null ? "" : categoria.trim().toLowerCase();
        if (!CATEGORIAS_VALIDAS.contains(cat)) {
            throw new IllegalArgumentException("Categoría inválida. Valores permitidos: cafe, pasteleria, merchandising");
        }
        return cat;
    }
}
