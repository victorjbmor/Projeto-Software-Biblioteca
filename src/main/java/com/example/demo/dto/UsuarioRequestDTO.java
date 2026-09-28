package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(
		@NotBlank(message = "nome obrigatorio") String nome,
		@NotBlank(message = "email obrigatorio") @Email(message = "email invalido") String email) {
}
