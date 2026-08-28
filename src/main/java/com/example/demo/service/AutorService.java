package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Autor;
import com.example.demo.repository.AutorRepository;

@Service
public class AutorService {

	private final AutorRepository autorRepository;

	public AutorService(AutorRepository autorRepository) {
		this.autorRepository = autorRepository;
	}

	public Autor salvar(Autor autor) {
		return autorRepository.save(autor);
	}
	
	public Autor findById(Long id) {
		return autorRepository.findById(id).orElseThrow();
	}
	
	public List<Autor> findAll() {
		return autorRepository.findAll();
	}
	
	public void deleteById(Long id) {
		findById(id);
		autorRepository.deleteById(id);
	}
	
}
