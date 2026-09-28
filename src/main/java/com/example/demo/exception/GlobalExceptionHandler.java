package com.example.demo.exception;

import java.time.Instant;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException ex) {
		List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
				.map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
				.toList();
		ErroResponse erro = new ErroResponse(Instant.now(), 400, "Erro de Validacao", "Dados invalidos", detalhes);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ResponseEntity<ErroResponse> recursoNaoEncontrado(RecursoNaoEncontradoException ex) {
		ErroResponse erro = new ErroResponse(404, "Not Found", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}

	@ExceptionHandler(RegraDeNegocioException.class)
	public ResponseEntity<ErroResponse> regraDeNegocio(RegraDeNegocioException ex) {
		ErroResponse erro = new ErroResponse(409, "Conflict", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
	}

	@ExceptionHandler(CredenciaisInvalidasException.class)
	public ResponseEntity<ErroResponse> credenciaisInvalidas(CredenciaisInvalidasException ex) {
		ErroResponse erro = new ErroResponse(401, "Unauthorized", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErroResponse> integridade(DataIntegrityViolationException ex) {
		ErroResponse erro = new ErroResponse(409, "Conflict",
				"Operacao viola a integridade dos dados (ex.: registro em uso por outro)");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
	}
}
