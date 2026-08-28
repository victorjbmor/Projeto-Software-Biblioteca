package com.example.demo.dto;

import com.example.demo.model.Livro;

public class LivroResponseDTO {

	private Long id;
	private String titulo;
	private String isbn;
	
	private String nomeAutor;
	
	public LivroResponseDTO() {
	}
	
	public LivroResponseDTO(Livro obj) {
		id = obj.getId();
		titulo = obj.getTitulo();
		isbn = obj.getIsbn();
		nomeAutor = obj.getAutor().getNome();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNomeAutor() {
		return nomeAutor;
	}

	public void setNomeAutor(String nomeAutor) {
		this.nomeAutor = nomeAutor;
	}
	
	
		
}
