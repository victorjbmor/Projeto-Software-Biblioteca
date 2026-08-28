package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public class EmprestimoRequestDTO {

	@NotNull
	private Long livroId;
	@NotNull
	private Long usuarioId;
	
	public EmprestimoRequestDTO() {
		
	}

	public EmprestimoRequestDTO(Long livroId, Long usuarioId) {
		this.livroId = livroId;
		this.usuarioId = usuarioId;
	}

	public Long getLivroId() {
		return livroId;
	}

	public void setLivroId(Long livroId) {
		this.livroId = livroId;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
}
