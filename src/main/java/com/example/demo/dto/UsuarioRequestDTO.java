package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioRequestDTO {

	@NotBlank(message="nome obrigatorio")
	private String nome;
	@NotBlank(message="email obrigatorio")
	@Email
	private String email;
	
	public UsuarioRequestDTO() {
	
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
