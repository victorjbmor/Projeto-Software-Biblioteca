package com.example.demo.dto;

import com.example.demo.model.Role;
import com.example.demo.model.Usuario;

// Repare que a senha NAO aparece aqui: nunca devolva o hash para o cliente
public record UsuarioResponseDTO(Long id, String nome, String email, Role role) {

	public UsuarioResponseDTO(Usuario obj) {
		this(obj.getId(), obj.getNome(), obj.getEmail(), obj.getRole());
	}
}
