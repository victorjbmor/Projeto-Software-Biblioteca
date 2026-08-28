package com.example.demo.exception;

import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResponseDTO> tratarValidacao(MethodArgumentNotValidException ex) {
		String mensagem = ex.getBindingResult().getFieldError().getDefaultMessage();
		ErroResponseDTO erro = new ErroResponseDTO(new Date(), 400, "Erro de Validacao", mensagem);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErroResponseDTO> recursoNaoEncontrado(NoSuchElementException ex) {
		String mensagem = "Livro nao encontrado";
		ErroResponseDTO erro = new ErroResponseDTO(new Date(), 404, "Not Found", mensagem);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
}
