package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.model.Emprestimo;

public record EmprestimoResponseDTO(
		Long id,
		String tituloLivro,
		String nomeUsuario,
		LocalDate dataEmprestimo,
		LocalDate dataPrevistaDevolucao,
		LocalDate dataDevolucaoReal) {

	public EmprestimoResponseDTO(Emprestimo obj) {
		this(obj.getId(), obj.getLivro().getTitulo(), obj.getUsuario().getNome(),
				obj.getDataEmprestimo(), obj.getDataPrevistaDevolucao(), obj.getDataDevolucaoReal());
	}
}
