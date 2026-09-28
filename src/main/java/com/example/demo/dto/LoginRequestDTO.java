package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
		@NotBlank(message = "email obrigatorio") String email,
		@NotBlank(message = "senha obrigatoria") String senha) {
}
