package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroRequestDTO(
		@NotBlank(message = "titulo obrigatorio") String titulo,
		@NotBlank(message = "isbn obrigatorio") String isbn,
		@NotNull(message = "autorId obrigatorio") Long autorId) {
}
