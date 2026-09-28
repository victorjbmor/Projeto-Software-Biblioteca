package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.RecursoNaoEncontradoException;
import com.example.demo.exception.RegraDeNegocioException;
import com.example.demo.model.Autor;
import com.example.demo.model.Livro;
import com.example.demo.repository.LivroRepository;

@Service
public class LivroService {

	private final LivroRepository livroRepository;
	private final AutorService autorService;

	public LivroService(LivroRepository livroRepository, AutorService autorService) {
		this.livroRepository = livroRepository;
		this.autorService = autorService;
	}

	@Transactional
	public Livro criar(String titulo, String isbn, Long autorId) {
		if (livroRepository.existsByIsbn(isbn)) {
			throw new RegraDeNegocioException("Ja existe um livro com o isbn " + isbn);
		}
		Autor autor = autorService.findById(autorId);
		return livroRepository.save(new Livro(titulo, isbn, autor));
	}

	@Transactional(readOnly = true)
	public Livro findById(Long id) {
		return livroRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Livro", id));
	}

	@Transactional
	public void deleteById(Long id) {
		findById(id);
		livroRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Livro> findAll() {
		return livroRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Livro> listarPorAutor(Long autorId) {
		autorService.findById(autorId);
		return livroRepository.findByAutorId(autorId);
	}

}
