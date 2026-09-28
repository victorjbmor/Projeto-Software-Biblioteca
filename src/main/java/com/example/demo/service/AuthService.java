package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.TokenResponseDTO;
import com.example.demo.exception.CredenciaisInvalidasException;
import com.example.demo.model.Role;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.TokenService;

@Service
public class AuthService {

	private final UsuarioService usuarioService;
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;

	public AuthService(UsuarioService usuarioService, UsuarioRepository usuarioRepository,
			PasswordEncoder passwordEncoder, TokenService tokenService) {
		this.usuarioService = usuarioService;
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenService = tokenService;
	}

	@Transactional
	public Usuario registrar(String nome, String email, String senha) {
		// Todo cadastro publico vira USER; o cliente nunca escolhe o proprio papel
		return usuarioService.salvar(new Usuario(nome, email, passwordEncoder.encode(senha), Role.USER));
	}

	@Transactional(readOnly = true)
	public TokenResponseDTO login(String email, String senha) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.filter(u -> passwordEncoder.matches(senha, u.getSenha()))
				// Mesma mensagem para "email nao existe" e "senha errada":
				// assim um atacante nao descobre quais emails estao cadastrados
				.orElseThrow(CredenciaisInvalidasException::new);
		return tokenService.gerarToken(usuario);
	}
}
