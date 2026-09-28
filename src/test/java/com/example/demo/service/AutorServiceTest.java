package com.example.demo.service;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.model.Autor;
import com.example.demo.repository.AutorRepository;

@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {

	@Mock
	private AutorRepository autorRepository;
	
	@InjectMocks
	private AutorService autorService;

	@Test
	void deveRetornarAutorQuandoIdExiste() {
		Autor autorTeste = new Autor("Machado de Assis", "Brasileiro");
		autorTeste.setId(1L);
		when(autorRepository.findById(1L)).thenReturn(Optional.of(autorTeste));
		
		Autor resultado = autorService.findById(1L)  ;
		
		assertEquals("Machado de Assis", resultado.getNome());
	}
	
	@Test
	void deveRetornarALista() {
		Autor autor1 = new Autor("Machado de Assis", "Brasileiro");
		Autor autor2 = new Autor("Carlos Drummond", "Brasileiro");
		List<Autor> listaTeste = new ArrayList<>();
		listaTeste.add(autor1);
		listaTeste.add(autor2);
		when(autorRepository.findAll()).thenReturn(listaTeste);
		
		List<Autor> resultado = autorService.findAll();
		
		assertEquals(listaTeste, resultado);
		
	}
	
	@Test
	void testarSave() {
		Autor autorTeste = new Autor("Machado de Assis", "Brasileiro");
		autorTeste.setId(1L);
		when(autorRepository.save(autorTeste)).thenReturn(autorTeste);
				
		Autor resultado = autorService.salvar(autorTeste);
		assertEquals(autorTeste,resultado);
	}
	
	@Test
	void testeDelete() {
		Autor autorTeste = new Autor("Machado de Assis", "Brasileiro");
		autorTeste.setId(1L);
		when(autorRepository.findById(1L)).thenReturn(Optional.of(autorTeste));
		autorService.deleteById(1L);
		verify(autorRepository).deleteById(1L);
	}
	
}