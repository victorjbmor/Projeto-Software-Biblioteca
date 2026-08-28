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

import com.example.demo.dto.LivroRequestDTO;
import com.example.demo.dto.LivroResponseDTO;
import com.example.demo.model.Autor;
import com.example.demo.model.Livro;
import com.example.demo.service.AutorService;
import com.example.demo.service.LivroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value="/livros")
public class LivroController {

	private final LivroService livroService;
	private final AutorService autorService;

	public LivroController(LivroService livroService, AutorService autorService) {
		this.livroService = livroService;
		this.autorService = autorService;
	}
	
	@PostMapping
	public ResponseEntity<LivroResponseDTO> criar(@Valid @RequestBody LivroRequestDTO obj) {
		Autor autor = autorService.findById(obj.getAutorId());
		Livro livro = new Livro(obj.getTitulo(), obj.getIsbn(), autor);
		Livro salvo = livroService.salvar(livro);
		LivroResponseDTO response = new LivroResponseDTO(salvo);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<LivroResponseDTO> findById(@PathVariable Long id) {
		Livro livro = livroService.findById(id);
		LivroResponseDTO response = new LivroResponseDTO(livro);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping
	public ResponseEntity<List<LivroResponseDTO>> findAll() {
		List<Livro> listaDeLivros = livroService.findAll();
		List<LivroResponseDTO> response = listaDeLivros.stream()
				.map(LivroResponseDTO::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		livroService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/autor/{id}")
	public ResponseEntity<List<LivroResponseDTO>> findByAutorId(@PathVariable Long id) {
		List<Livro> listaDeLivros = livroService.listarPorAutor(id);
		List<LivroResponseDTO> response = listaDeLivros.stream()
				.map(LivroResponseDTO::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}
	
}
