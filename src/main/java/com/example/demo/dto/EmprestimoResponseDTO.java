package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.model.Emprestimo;

public class EmprestimoResponseDTO {

	private Long id;
	private String tituloLivro;
	private String nomeUsuario;
	private LocalDate dataEmprestimo;
	private LocalDate dataPrevistaDevolucao;
	private LocalDate dataDevolucaoReal;
	
	public EmprestimoResponseDTO() {
	}
	
	public EmprestimoResponseDTO(Emprestimo obj) {
		id = obj.getId();
		tituloLivro = obj.getLivro().getTitulo();
		nomeUsuario = obj.getUsuario().getNome();
		dataEmprestimo = obj.getDataEmprestimo();
		dataPrevistaDevolucao = obj.getDataPrevistaDevolucao();
		dataDevolucaoReal = obj.getDataDevolucaoReal();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTituloLivro() {
		return tituloLivro;
	}

	public void setTituloLivro(String tituloLivro) {
		this.tituloLivro = tituloLivro;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(LocalDate dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public LocalDate getDataPrevistaDevolucao() {
		return dataPrevistaDevolucao;
	}

	public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
		this.dataPrevistaDevolucao = dataPrevistaDevolucao;
	}

	public LocalDate getDataDevolucaoReal() {
		return dataDevolucaoReal;
	}

	public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
		this.dataDevolucaoReal = dataDevolucaoReal;
	}
	
}
