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

import com.example.demo.dto.AutorRequestDTO;
import com.example.demo.dto.AutorResponseDTO;
import com.example.demo.model.Autor;
import com.example.demo.service.AutorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value="/autors")
public class AutorController {

	private final AutorService autorService;

	public AutorController(AutorService autorService) {
		this.autorService = autorService;
	}
	
	@PostMapping
	public ResponseEntity<AutorResponseDTO> criar(@Valid @RequestBody AutorRequestDTO obj) {
		Autor autor = new Autor(obj.getNome(), obj.getNacionalidade());
		Autor salvo = autorService.salvar(autor);
		AutorResponseDTO response = new AutorResponseDTO(salvo);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AutorResponseDTO> findById(@PathVariable Long id) {
		Autor autor = autorService.findById(id);
		AutorResponseDTO response = new AutorResponseDTO(autor);
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping()
	public ResponseEntity<List<AutorResponseDTO>> findAll() {
		List<Autor> lista = autorService.findAll();
		List<AutorResponseDTO> response = lista.stream()
				.map(AutorResponseDTO::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		autorService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	
}
