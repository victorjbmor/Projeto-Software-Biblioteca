package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.RecursoNaoEncontradoException;
import com.example.demo.exception.RegraDeNegocioException;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Transactional
	public Usuario salvar(Usuario usuario) {
		if (usuarioRepository.existsByEmail(usuario.getEmail())) {
			throw new RegraDeNegocioException("Email " + usuario.getEmail() + " ja cadastrado");
		}
		return usuarioRepository.save(usuario);
	}

	@Transactional(readOnly = true)
	public Usuario findById(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", id));
	}

	@Transactional(readOnly = true)
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	@Transactional
	public void deleteById(Long id) {
		findById(id);
		usuarioRepository.deleteById(id);
	}

}
