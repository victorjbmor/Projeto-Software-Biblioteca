package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.RecursoNaoEncontradoException;
import com.example.demo.model.Autor;
import com.example.demo.repository.AutorRepository;

@Service
public class AutorService {

	private final AutorRepository autorRepository;

	public AutorService(AutorRepository autorRepository) {
		this.autorRepository = autorRepository;
	}

	@Transactional
	public Autor salvar(Autor autor) {
		return autorRepository.save(autor);
	}

	@Transactional(readOnly = true)
	public Autor findById(Long id) {
		return autorRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Autor", id));
	}

	@Transactional(readOnly = true)
	public List<Autor> findAll() {
		return autorRepository.findAll();
	}

	@Transactional
	public void deleteById(Long id) {
		findById(id);
		autorRepository.deleteById(id);
	}

}
