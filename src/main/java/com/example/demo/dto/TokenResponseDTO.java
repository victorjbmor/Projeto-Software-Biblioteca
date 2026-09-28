package com.example.demo.dto;

import java.time.Instant;

public record TokenResponseDTO(String token, String tipo, Instant expiraEm) {
}
