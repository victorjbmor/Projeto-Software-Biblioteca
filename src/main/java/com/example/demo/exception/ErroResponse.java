package com.example.demo.exception;

import java.time.Instant;
import java.util.List;

public record ErroResponse(Instant timestamp, int status, String error, String message, List<String> detalhes) {

	public ErroResponse(int status, String error, String message) {
		this(Instant.now(), status, error, message, List.of());
	}
}
