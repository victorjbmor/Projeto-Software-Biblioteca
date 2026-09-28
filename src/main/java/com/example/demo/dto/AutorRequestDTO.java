package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record AutorRequestDTO(
		@NotBlank(message = "nome obrigatorio") String nome,
		@NotBlank(message = "nacionalidade obrigatoria") String nacionalidade) {
}
