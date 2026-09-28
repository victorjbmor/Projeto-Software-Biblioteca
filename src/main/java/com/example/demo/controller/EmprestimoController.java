package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
@RequestMapping("/emprestimos")
public class EmprestimoController {

	private final EmprestimoService emprestimoService;

	public EmprestimoController(EmprestimoService emprestimoService) {
		this.emprestimoService = emprestimoService;
	}

	// O "subject" do token e o id do usuario logado. Pegar o usuario daqui
	// (e nao do corpo) impede que alguem crie emprestimos em nome de outra pessoa
	@PostMapping
	public ResponseEntity<EmprestimoResponseDTO> criar(@Valid @RequestBody EmprestimoRequestDTO obj,
			@AuthenticationPrincipal Jwt jwt) {
		Emprestimo emprestimo = emprestimoService.criar(obj.livroId(), Long.valueOf(jwt.getSubject()));
		return ResponseEntity.status(HttpStatus.CREATED).body(new EmprestimoResponseDTO(emprestimo));
	}

	@GetMapping("/meus")
	public ResponseEntity<List<EmprestimoResponseDTO>> meus(@AuthenticationPrincipal Jwt jwt) {
		List<EmprestimoResponseDTO> response = emprestimoService.listarPorUsuario(Long.valueOf(jwt.getSubject())).stream()
				.map(EmprestimoResponseDTO::new)
				.toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmprestimoResponseDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(new EmprestimoResponseDTO(emprestimoService.findById(id)));
	}

	@GetMapping
	public ResponseEntity<List<EmprestimoResponseDTO>> findAll() {
		List<EmprestimoResponseDTO> response = emprestimoService.findAll().stream()
				.map(EmprestimoResponseDTO::new)
				.toList();
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}/devolver")
	public ResponseEntity<EmprestimoResponseDTO> devolver(@PathVariable Long id) {
		return ResponseEntity.ok(new EmprestimoResponseDTO(emprestimoService.devolver(id)));
	}
}
