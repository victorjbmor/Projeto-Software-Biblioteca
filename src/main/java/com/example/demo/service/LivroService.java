package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Livro;
import com.example.demo.repository.LivroRepository;

@Service
public class LivroService {

	private final LivroRepository livroRepository;

	public LivroService(LivroRepository livroRepository) {
		this.livroRepository = livroRepository;
	}

	public Livro salvar (Livro livro) {
		return livroRepository.save(livro);
	}
	
	public Livro findById(Long id) {
		return livroRepository.findById(id).orElseThrow();
	}
	
	public void deleteById(Long id) {
		findById(id);
		livroRepository.deleteById(id);
	}
	
	public List<Livro> findAll() {
		return livroRepository.findAll();
	}
	
	public List<Livro> listarPorAutor(Long autorId) {
		return livroRepository.findByAutorId(autorId);
	}
		
}
