package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public record EmprestimoRequestDTO(
		@NotNull(message = "livroId obrigatorio") Long livroId,
		@NotNull(message = "usuarioId obrigatorio") Long usuarioId) {
}
