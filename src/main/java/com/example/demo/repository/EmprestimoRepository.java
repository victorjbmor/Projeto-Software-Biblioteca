package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Emprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

	// Spring Data gera a query a partir do nome do metodo:
	// "existe emprestimo com esse livro E sem data de devolucao?"
	boolean existsByLivroIdAndDataDevolucaoRealIsNull(Long livroId);

	List<Emprestimo> findByUsuarioId(Long usuarioId);
}
