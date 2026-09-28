package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.RecursoNaoEncontradoException;
import com.example.demo.exception.RegraDeNegocioException;
import com.example.demo.model.Emprestimo;
import com.example.demo.model.Livro;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EmprestimoRepository;

@Service
public class EmprestimoService {

	private static final int PRAZO_DEVOLUCAO_DIAS = 14;

	private final EmprestimoRepository emprestimoRepository;
	private final LivroService livroService;
	private final UsuarioService usuarioService;

	public EmprestimoService(EmprestimoRepository emprestimoRepository, UsuarioService usuarioService, LivroService livroService) {
		this.emprestimoRepository = emprestimoRepository;
		this.livroService = livroService;
		this.usuarioService = usuarioService;
	}

	@Transactional(readOnly = true)
	public Emprestimo findById(Long id) {
		return emprestimoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Emprestimo", id));
	}

	@Transactional(readOnly = true)
	public List<Emprestimo> findAll() {
		return emprestimoRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Emprestimo> listarPorUsuario(Long usuarioId) {
		return emprestimoRepository.findByUsuarioId(usuarioId);
	}

	@Transactional
	public Emprestimo criar(Long livroId, Long usuarioId) {
		Livro livro = livroService.findById(livroId);
		Usuario usuario = usuarioService.findById(usuarioId);
		if (emprestimoRepository.existsByLivroIdAndDataDevolucaoRealIsNull(livroId)) {
			throw new RegraDeNegocioException("Livro '" + livro.getTitulo() + "' ja esta emprestado");
		}
		LocalDate dataEmprestimo = LocalDate.now();
		LocalDate dataPrevistaDevolucao = dataEmprestimo.plusDays(PRAZO_DEVOLUCAO_DIAS);
		Emprestimo emprestimo = new Emprestimo(livro, usuario, dataEmprestimo, dataPrevistaDevolucao);
		return emprestimoRepository.save(emprestimo);
	}

	@Transactional
	public Emprestimo devolver(Long id) {
		Emprestimo emprestimo = findById(id);
		emprestimo.devolver(LocalDate.now());
		// Nao precisa chamar save(): dentro de @Transactional o JPA detecta a
		// alteracao na entidade (dirty checking) e faz o UPDATE no commit.
		return emprestimo;
	}

}
