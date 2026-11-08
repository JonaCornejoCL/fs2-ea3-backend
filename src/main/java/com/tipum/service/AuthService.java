package com.tipum.service;

import com.tipum.dto.LoginRequest;
import com.tipum.dto.LoginResponse;
import com.tipum.dto.RegisterRequest;
import com.tipum.dto.UsuarioDto;
import com.tipum.entity.Rol;
import com.tipum.entity.Usuario;
import com.tipum.repository.UsuarioRepository;
import com.tipum.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public UsuarioDto registrar(RegisterRequest req) {
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
                .rol(Rol.CLIENTE)
                .build();

        return UsuarioDto.from(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req) {
        Usuario usuario = usuarioRepository.findByCorreo(req.correo().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(req.password(), usuario.getPassword())) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        String token = jwtService.generateToken(usuario);
        return LoginResponse.of(token, UsuarioDto.from(usuario));
    }
}
