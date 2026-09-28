package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.RegistroRequestDTO;
import com.example.demo.dto.TokenResponseDTO;
import com.example.demo.dto.UsuarioResponseDTO;
import com.example.demo.model.Usuario;
import com.example.demo.service.AuthService;
import com.example.demo.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final UsuarioService usuarioService;

	public AuthController(AuthService authService, UsuarioService usuarioService) {
		this.authService = authService;
		this.usuarioService = usuarioService;
	}

	@PostMapping("/registrar")
	public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO obj) {
		Usuario usuario = authService.registrar(obj.nome(), obj.email(), obj.senha());
		return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(usuario));
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO obj) {
		return ResponseEntity.ok(authService.login(obj.email(), obj.senha()));
	}

	// @AuthenticationPrincipal injeta o token ja validado pelo Spring Security
	@GetMapping("/me")
	public ResponseEntity<UsuarioResponseDTO> me(@AuthenticationPrincipal Jwt jwt) {
		Usuario usuario = usuarioService.findById(Long.valueOf(jwt.getSubject()));
		return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
	}
}
