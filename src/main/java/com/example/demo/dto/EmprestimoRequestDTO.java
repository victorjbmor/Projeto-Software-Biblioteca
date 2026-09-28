package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

// O usuario do emprestimo vem do token JWT, nao do corpo da requisicao
public record EmprestimoRequestDTO(@NotNull(message = "livroId obrigatorio") Long livroId) {
}
