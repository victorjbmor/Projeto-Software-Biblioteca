package com.example.demo.dto;

import com.example.demo.model.Autor;

public class AutorResponseDTO {
	
	private Long id;
	private String nome;
	private String nacionalidade;
	
	public AutorResponseDTO() {
	}

	public AutorResponseDTO(Autor obj) {
		id = obj.getId();
		nome = obj.getNome();
		nacionalidade = obj.getNacionalidade();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
