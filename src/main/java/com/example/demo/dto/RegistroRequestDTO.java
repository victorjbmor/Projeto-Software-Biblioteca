package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequestDTO(
		@NotBlank(message = "nome obrigatorio") String nome,
		@NotBlank(message = "email obrigatorio") @Email(message = "email invalido") String email,
		@NotBlank(message = "senha obrigatoria") @Size(min = 6, message = "senha deve ter no minimo 6 caracteres") String senha) {
}
