package com.tipum.service;

import com.tipum.dto.UsuarioDto;
import com.tipum.dto.UsuarioRequest;
import com.tipum.entity.Rol;
import com.tipum.entity.Usuario;
import com.tipum.exception.ResourceNotFoundException;
import com.tipum.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> listar() {
        return usuarioRepository.findAll().stream().map(UsuarioDto::from).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDto obtener(Long id) {
        return UsuarioDto.from(buscar(id));
    }

    @Transactional
    public UsuarioDto crear(UsuarioRequest req) {
        if (usuarioRepository.existsByCorreo(req.correo().toLowerCase())) {
            throw new IllegalArgumentException("El correo ya se encuentra registrado");
        }
        if (usuarioRepository.existsByRun(req.run().toUpperCase())) {
            throw new IllegalArgumentException("El RUN ya se encuentra registrado");
        }

        Usuario usuario = Usuario.builder()
                .run(req.run().toUpperCase())
                .nombre(req.nombre())
                .apellidos(req.apellidos())
                .correo(req.correo().toLowerCase())
                .password(passwordEncoder.encode(req.password()))
                .region(req.region())
                .comuna(req.comuna())
                .direccion(req.direccion())
                .fechaNacimiento(req.fechaNacimiento())
                .rol(parseRol(req.rol()))
                .build();

        return UsuarioDto.from(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDto actualizar(Long id, UsuarioRequest req) {
        Usuario usuario = buscar(id);

        String nuevoCorreo = req.correo().toLowerCase();
        if (!usuario.getCorreo().equals(nuevoCorreo) && usuarioRepository.existsByCorreo(nuevoCorreo)) {
            throw new IllegalArgumentException("El correo ya se encuentra registrado");
        }

        usuario.setRun(req.run().toUpperCase());
        usuario.setNombre(req.nombre());
        usuario.setApellidos(req.apellidos());
        usuario.setCorreo(nuevoCorreo);
        if (req.password() != null && !req.password().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(req.password()));
        }
        usuario.setRegion(req.region());
        usuario.setComuna(req.comuna());
        usuario.setDireccion(req.direccion());
        usuario.setFechaNacimiento(req.fechaNacimiento());
        usuario.setRol(parseRol(req.rol()));

        return UsuarioDto.from(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminar(Long id) {
        usuarioRepository.delete(buscar(id));
    }

    private Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
    }

    private Rol parseRol(String rol) {
        try {
            return Rol.valueOf(rol.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Rol inválido. Valores permitidos: ADMIN, VENDEDOR, CLIENTE");
        }
    }
}
