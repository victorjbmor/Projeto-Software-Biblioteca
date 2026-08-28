package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class LivroRequestDTO {

	@NotBlank(message="titulo obrigatorio")
	private String titulo;
	@NotBlank(message="isbn obrigatorio")
	private String isbn;
	private Long autorId;

	public LivroRequestDTO() {
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public String getIsbn() {
		return isbn;
	}


	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}


	public Long getAutorId() {
		return autorId;
	}


	public void setAutorId(Long autorId) {
		this.autorId = autorId;
	}
}
