package com.example.demo.dto;

import com.example.demo.model.Livro;

public record LivroResponseDTO(Long id, String titulo, String isbn, String nomeAutor) {

	public LivroResponseDTO(Livro obj) {
		this(obj.getId(), obj.getTitulo(), obj.getIsbn(), obj.getAutor().getNome());
	}
}
