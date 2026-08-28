package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Emprestimo;
import com.example.demo.model.Livro;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EmprestimoRepository;

@Service
public class EmprestimoService {

	private final EmprestimoRepository emprestimoRepository;
	private final LivroService livroService;
	private final UsuarioService usuarioService;

	public EmprestimoService(EmprestimoRepository emprestimoRepository, UsuarioService usuarioService, LivroService livroService) {
		this.emprestimoRepository = emprestimoRepository;
		this.livroService = livroService;
		this.usuarioService = usuarioService;
	}
	
	public Emprestimo salvar(Emprestimo emprestimo) {
		return emprestimoRepository.save(emprestimo);
	}
	
	public Emprestimo findById(Long id) {
		return emprestimoRepository.findById(id).orElseThrow();
	}
	
	public List<Emprestimo> findAll() {
		return emprestimoRepository.findAll();
	}
	
	public void deleteById(Long id) {
		findById(id);
		emprestimoRepository.deleteById(id);
	}
	
	public Emprestimo criar(Long livroId, Long usuarioId) {
		Livro livro = livroService.findById(livroId);
		Usuario usuario = usuarioService.findById(usuarioId);
		LocalDate dataEmprestimo = LocalDate.now();
		LocalDate dataPrevistaDevolucao = dataEmprestimo.plusDays(14);
		Emprestimo emprestimo = new Emprestimo(livro,usuario,dataEmprestimo,dataPrevistaDevolucao);
		return emprestimoRepository.save(emprestimo);
	}
	
	public Emprestimo devolver(Long id) {
		Emprestimo emprestimo = findById(id);
		emprestimo.setDataDevolucaoReal(LocalDate.now());
		return salvar(emprestimo);
	}
	
}
