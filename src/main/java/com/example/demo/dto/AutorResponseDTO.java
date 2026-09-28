package com.example.demo.dto;

import com.example.demo.model.Autor;

public record AutorResponseDTO(Long id, String nome, String nacionalidade) {

	public AutorResponseDTO(Autor obj) {
		this(obj.getId(), obj.getNome(), obj.getNacionalidade());
	}
}
