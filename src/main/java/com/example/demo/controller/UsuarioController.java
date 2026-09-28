package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UsuarioRequestDTO;
import com.example.demo.dto.UsuarioResponseDTO;
import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PostMapping
	public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO obj) {
		Usuario salvo = usuarioService.salvar(new Usuario(obj.nome(), obj.email()));
		return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(salvo));
	}

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
