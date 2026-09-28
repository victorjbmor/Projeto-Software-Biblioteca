package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.exception.RecursoNaoEncontradoException;
import com.example.demo.exception.RegraDeNegocioException;
import com.example.demo.model.Autor;
import com.example.demo.model.Emprestimo;
import com.example.demo.model.Livro;
import com.example.demo.model.Role;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EmprestimoRepository;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

	@Mock
	private EmprestimoRepository emprestimoRepository;

	@Mock
	private LivroService livroService;

	@Mock
	private UsuarioService usuarioService;

	@InjectMocks
	private EmprestimoService emprestimoService;

	private final Livro livro = new Livro("Dom Casmurro", "978-85", new Autor("Machado de Assis", "Brasileiro"));
	private final Usuario usuario = new Usuario("Ana", "ana@email.com", "hash", Role.USER);

	@Test
	void deveCriarEmprestimoComPrazoDe14Dias() {
		when(livroService.findById(1L)).thenReturn(livro);
		when(usuarioService.findById(2L)).thenReturn(usuario);
		when(emprestimoRepository.existsByLivroIdAndDataDevolucaoRealIsNull(1L)).thenReturn(false);
		when(emprestimoRepository.save(any(Emprestimo.class))).thenAnswer(inv -> inv.getArgument(0));

		Emprestimo resultado = emprestimoService.criar(1L, 2L);

		assertEquals(LocalDate.now().plusDays(14), resultado.getDataPrevistaDevolucao());
	}

	@Test
	void naoDeveEmprestarLivroQueJaEstaEmprestado() {
		when(livroService.findById(1L)).thenReturn(livro);
		when(usuarioService.findById(2L)).thenReturn(usuario);
		when(emprestimoRepository.existsByLivroIdAndDataDevolucaoRealIsNull(1L)).thenReturn(true);

		assertThrows(RegraDeNegocioException.class, () -> emprestimoService.criar(1L, 2L));
		verify(emprestimoRepository, never()).save(any());
	}

	@Test
	void deveDevolverEmprestimoAtivo() {
		Emprestimo emprestimo = new Emprestimo(livro, usuario, LocalDate.now(), LocalDate.now().plusDays(14));
		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

		Emprestimo resultado = emprestimoService.devolver(1L);

		assertNotNull(resultado.getDataDevolucaoReal());
	}

	@Test
	void naoDeveDevolverDuasVezes() {
		Emprestimo emprestimo = new Emprestimo(livro, usuario, LocalDate.now(), LocalDate.now().plusDays(14));
		emprestimo.devolver(LocalDate.now());
		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

		assertThrows(RegraDeNegocioException.class, () -> emprestimoService.devolver(1L));
	}

	@Test
	void deveLancar404QuandoEmprestimoNaoExiste() {
		when(emprestimoRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> emprestimoService.findById(99L));
	}
}
