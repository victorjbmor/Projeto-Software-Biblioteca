package com.example.demo.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

	public RecursoNaoEncontradoException(String recurso, Long id) {
		super(recurso + " com id " + id + " nao encontrado");
	}
}
