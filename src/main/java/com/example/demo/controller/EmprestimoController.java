package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EmprestimoRequestDTO;
import com.example.demo.dto.EmprestimoResponseDTO;
import com.example.demo.model.Emprestimo;
import com.example.demo.service.EmprestimoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value="/emprestimo")
public class EmprestimoController {

	private final EmprestimoService emprestimoService;

	public EmprestimoController(EmprestimoService emprestimoService) {
		this.emprestimoService = emprestimoService;
	}
	
	@PostMapping
	public ResponseEntity<EmprestimoResponseDTO> criar(@Valid @RequestBody EmprestimoRequestDTO obj) {
		Emprestimo emprestimo = emprestimoService.criar(obj.getLivroId(), obj.getUsuarioId());
		EmprestimoResponseDTO response = new EmprestimoResponseDTO(emprestimo);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EmprestimoResponseDTO> findById(@PathVariable Long id) {
		Emprestimo emprestimo = emprestimoService.findById(id);
		EmprestimoResponseDTO response = new EmprestimoResponseDTO(emprestimo);
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping()
	public ResponseEntity<List<EmprestimoResponseDTO>> findAll() {
		List<Emprestimo> listaDeEmprestimo = emprestimoService.findAll();
		List<EmprestimoResponseDTO> response = listaDeEmprestimo.stream()
				.map(EmprestimoResponseDTO::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok(response);
		
	}
	
	@PutMapping("/{id}/devolver")
	public ResponseEntity<EmprestimoResponseDTO> devolver(@PathVariable Long id) {
		Emprestimo emprestimo = emprestimoService.devolver(id);	
		EmprestimoResponseDTO response = new EmprestimoResponseDTO(emprestimo);
		return ResponseEntity.ok().body(response);
	}
}
