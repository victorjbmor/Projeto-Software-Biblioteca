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

import com.example.demo.dto.LivroRequestDTO;
import com.example.demo.dto.LivroResponseDTO;
import com.example.demo.model.Livro;
import com.example.demo.service.LivroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/livros")
public class LivroController {

	private final LivroService livroService;

	public LivroController(LivroService livroService) {
		this.livroService = livroService;
	}

	@PostMapping
	public ResponseEntity<LivroResponseDTO> criar(@Valid @RequestBody LivroRequestDTO obj) {
		Livro salvo = livroService.criar(obj.titulo(), obj.isbn(), obj.autorId());
		return ResponseEntity.status(HttpStatus.CREATED).body(new LivroResponseDTO(salvo));
	}

	@GetMapping("/{id}")
	public ResponseEntity<LivroResponseDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(new LivroResponseDTO(livroService.findById(id)));
	}

	@GetMapping
	public ResponseEntity<List<LivroResponseDTO>> findAll() {
		return ResponseEntity.ok(toResponse(livroService.findAll()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		livroService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/autor/{id}")
	public ResponseEntity<List<LivroResponseDTO>> findByAutorId(@PathVariable Long id) {
		return ResponseEntity.ok(toResponse(livroService.listarPorAutor(id)));
	}

	private List<LivroResponseDTO> toResponse(List<Livro> livros) {
		return livros.stream().map(LivroResponseDTO::new).toList();
	}

}
