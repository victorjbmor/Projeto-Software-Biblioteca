package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UsuarioResponseDTO;
import com.example.demo.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	// O cadastro de usuarios agora e feito em POST /auth/registrar

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(new UsuarioResponseDTO(usuarioService.findById(id)));
	}

	@GetMapping
	public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
		List<UsuarioResponseDTO> response = usuarioService.findAll().stream()
				.map(UsuarioResponseDTO::new)
				.toList();
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		usuarioService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
