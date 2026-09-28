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

import com.example.demo.dto.AutorRequestDTO;
import com.example.demo.dto.AutorResponseDTO;
import com.example.demo.model.Autor;
import com.example.demo.service.AutorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/autores")
public class AutorController {

	private final AutorService autorService;

	public AutorController(AutorService autorService) {
		this.autorService = autorService;
	}

	@PostMapping
	public ResponseEntity<AutorResponseDTO> criar(@Valid @RequestBody AutorRequestDTO obj) {
		Autor salvo = autorService.salvar(new Autor(obj.nome(), obj.nacionalidade()));
		return ResponseEntity.status(HttpStatus.CREATED).body(new AutorResponseDTO(salvo));
	}

	@GetMapping("/{id}")
	public ResponseEntity<AutorResponseDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(new AutorResponseDTO(autorService.findById(id)));
	}

	@GetMapping
	public ResponseEntity<List<AutorResponseDTO>> findAll() {
		List<AutorResponseDTO> response = autorService.findAll().stream()
				.map(AutorResponseDTO::new)
				.toList();
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		autorService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
