package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

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
@RequestMapping(value="/usuario")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@PostMapping
	public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO obj) {
		Usuario usuario = new Usuario(obj.getNome(), obj.getEmail());
		Usuario usuarioSalvo = usuarioService.salvar(usuario);
		UsuarioResponseDTO response = new UsuarioResponseDTO(usuarioSalvo);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id) {
		Usuario usuario = usuarioService.findById(id);
		UsuarioResponseDTO response = new UsuarioResponseDTO(usuario);
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping
	public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
		List<Usuario> listaDeUsuario = usuarioService.findAll();
		List<UsuarioResponseDTO> response = listaDeUsuario.stream()
				.map(UsuarioResponseDTO::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		usuarioService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}
