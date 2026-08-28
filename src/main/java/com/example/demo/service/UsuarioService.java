package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	public Usuario salvar(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	public Usuario findById(Long id) {
		return usuarioRepository.findById(id).orElseThrow();
	}
	
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}
	
	public void deleteById(Long id) {
		findById(id);
		usuarioRepository.deleteById(id);
	}
	
	
	
}
