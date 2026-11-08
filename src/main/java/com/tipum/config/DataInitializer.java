package com.tipum.config;

import com.tipum.entity.DetalleOrden;
import com.tipum.entity.Orden;
import com.tipum.entity.Producto;
import com.tipum.entity.Rol;
import com.tipum.entity.Usuario;
import com.tipum.repository.OrdenRepository;
import com.tipum.repository.ProductoRepository;
import com.tipum.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final OrdenRepository ordenRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           ProductoRepository productoRepository,
                           OrdenRepository ordenRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.ordenRepository = ordenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        Usuario admin = crearUsuario("159876543", "Admin", "Master", "admin@duoc.cl", "admin123",
                Rol.ADMIN, "Región de Valparaíso", "Viña del Mar", "Álvarez 2336");
        Usuario vendedor = crearUsuario("191112223", "Vendedor", "TiPum", "vendedor@duoc.cl", "vendedor123",
                Rol.VENDEDOR, "Región Metropolitana", "Santiago", "Av. Providencia 100");
        Usuario juan = crearUsuario("19011022K", "Juan", "Pérez", "juan.perez@duoc.cl", "cliente123",
                Rol.CLIENTE, "Región Metropolitana", "Santiago", "Av. España 123");
        Usuario maria = crearUsuario("182345678", "María", "González", "m.gonzalez@gmail.com", "cliente123",
                Rol.CLIENTE, "Región de Valparaíso", "Quilpué", "Los Carrera 450");

        Producto p1 = producto("Café Latte Pumita", "cafe", 2500, 15, 5, true, 2000,
                "https://images.unsplash.com/photo-1517701604599-bb29b565090c?auto=format&fit=crop&q=80",
                "El favorito de la casa. Un exquisito café de especialidad preparado con granos arábicos seleccionados, leche texturizada y un toque de amor inspirado en nuestro querido Pumita.");
        Producto p2 = producto("Espresso Titi", "cafe", 1800, 4, 5, false, 1800,
                "https://images.unsplash.com/photo-1510591509098-f4fdc6d0ff04?auto=format&fit=crop&q=80",
                "Fuerte, intenso y con mucho carácter. Nuestro Espresso Titi es un shot de pura energía extraído a la perfección con notas achocolatadas.");
        Producto p3 = producto("Muffin de Arándanos", "pasteleria", 2200, 12, 4, true, 1800,
                "https://images.unsplash.com/photo-1607958996333-41aef7caefaa?auto=format&fit=crop&q=80",
                "Esponjoso por dentro, doradito por fuera. Horneamos nuestros muffins a diario utilizando arándanos frescos de temporada y una receta artesanal.");
        Producto p4 = producto("Cheesecake de Frambuesa", "pasteleria", 3500, 3, 5, false, 3500,
                "https://images.unsplash.com/photo-1565958011703-44f9829ba187?auto=format&fit=crop&q=80",
                "Nuestra joya de la pastelería. Una base crujiente de galleta, un relleno cremoso de queso premium y un coulis casero de frambuesas frescas.");
        Producto p5 = producto("Tazón Oficial Cafeta TiPum", "merchandising", 4990, 20, 5, true, 3990,
                "https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?auto=format&fit=crop&q=80",
                "Tazón de cerámica de alta resistencia con el logo de Cafeta TiPum para que disfrutes tu café en casa o en clases.");

        productoRepository.saveAll(List.of(p1, p2, p3, p4, p5));

        Orden o1 = Orden.builder()
                .codigo("ORDER1001")
                .usuario(juan)
                .fecha(LocalDateTime.of(2026, 9, 12, 10, 30))
                .monto(8990)
                .estado("Completado")
                .direccion("Av. España 123, Santiago")
                .build();
        o1.agregarItem(DetalleOrden.builder().nombre("Café Latte Pumita").cantidad(2).precio(2500).build());
        o1.agregarItem(DetalleOrden.builder().nombre("Tazón Oficial Cafeta TiPum").cantidad(1).precio(3990).build());

        Orden o2 = Orden.builder()
                .codigo("ORDER1002")
                .usuario(maria)
                .fecha(LocalDateTime.of(2026, 9, 12, 11, 15))
                .monto(5700)
                .estado("Completado")
                .direccion("Los Carrera 450, Quilpué")
                .build();
        o2.agregarItem(DetalleOrden.builder().nombre("Cheesecake de Frambuesa").cantidad(1).precio(3500).build());
        o2.agregarItem(DetalleOrden.builder().nombre("Muffin de Arándanos").cantidad(1).precio(2200).build());

        ordenRepository.saveAll(List.of(o1, o2));
    }

    private Usuario crearUsuario(String run, String nombre, String apellidos, String correo,
                                 String password, Rol rol, String region, String comuna, String direccion) {
        return usuarioRepository.save(Usuario.builder()
                .run(run)
                .nombre(nombre)
                .apellidos(apellidos)
                .correo(correo)
                .password(passwordEncoder.encode(password))
                .region(region)
                .comuna(comuna)
                .direccion(direccion)
                .rol(rol)
                .build());
    }

    private Producto producto(String nombre, String categoria, int precio, int stock, int stockCritico,
                              boolean enOferta, int precioOferta, String imagen, String descripcion) {
        return Producto.builder()
                .nombre(nombre)
                .categoria(categoria)
                .precio(precio)
                .stock(stock)
                .stockCritico(stockCritico)
                .enOferta(enOferta)
                .precioOferta(precioOferta)
                .imagen(imagen)
                .descripcion(descripcion)
                .build();
    }
}
