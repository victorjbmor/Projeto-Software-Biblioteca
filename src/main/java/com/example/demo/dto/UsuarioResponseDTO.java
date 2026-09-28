package com.example.demo.dto;

import com.example.demo.model.Usuario;

public record UsuarioResponseDTO(Long id, String nome, String email) {

	public UsuarioResponseDTO(Usuario obj) {
		this(obj.getId(), obj.getNome(), obj.getEmail());
	}
}
