package com.example.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Role;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

// Cria um ADMIN ao subir a aplicacao, ja que o /auth/registrar so cria USER
@Component
public class AdminInicializador implements CommandLineRunner {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final String email;
	private final String senha;

	public AdminInicializador(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
			@Value("${biblioteca.admin.email}") String email, @Value("${biblioteca.admin.senha}") String senha) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.email = email;
		this.senha = senha;
	}

	@Override
	public void run(String... args) {
		if (!usuarioRepository.existsByEmail(email)) {
			usuarioRepository.save(new Usuario("Administrador", email, passwordEncoder.encode(senha), Role.ADMIN));
		}
	}
}
