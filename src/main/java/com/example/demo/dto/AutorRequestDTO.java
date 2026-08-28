package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class AutorRequestDTO {

	@NotBlank(message="nome obrigatorio")
	private String nome;
	@NotBlank(message="nacionalidade obrigatorio")
	private String nacionalidade;
	
	public AutorRequestDTO() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}
	
}
