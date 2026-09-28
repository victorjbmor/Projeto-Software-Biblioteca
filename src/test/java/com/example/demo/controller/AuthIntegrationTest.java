package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private String login(String email, String senha) throws Exception {
		String body = mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"email": "%s", "senha": "%s"}
						""".formatted(email, senha)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		return JsonPath.read(body, "$.token");
	}

	@Test
	void rotaProtegidaSemTokenRetorna401() throws Exception {
		mockMvc.perform(get("/auth/me")).andExpect(status().isUnauthorized());
	}

	@Test
	void tokenInvalidoRetorna401() throws Exception {
		mockMvc.perform(get("/auth/me").header("Authorization", "Bearer token.falso.aqui"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void listarLivrosEPublico() throws Exception {
		mockMvc.perform(get("/livros")).andExpect(status().isOk());
	}

	@Test
	void senhaErradaRetorna401() throws Exception {
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"email": "admin@biblioteca.com", "senha": "errada"}
						"""))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void fluxoCompletoRegistrarLogarEAcessar() throws Exception {
		mockMvc.perform(post("/auth/registrar")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"nome": "Maria", "email": "maria@email.com", "senha": "123456"}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.role").value("USER"))
				.andExpect(jsonPath("$.senha").doesNotExist());

		String token = login("maria@email.com", "123456");

		mockMvc.perform(get("/auth/me").header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("maria@email.com"));

		// USER nao pode acessar rota de ADMIN
		mockMvc.perform(get("/usuarios").header("Authorization", "Bearer " + token))
				.andExpect(status().isForbidden());
	}

	@Test
	void adminPodeCadastrarAutor() throws Exception {
		String token = login("admin@biblioteca.com", "admin123");

		mockMvc.perform(post("/autores")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"nome": "Clarice Lispector", "nacionalidade": "Brasileira"}
						"""))
				.andExpect(status().isCreated());
	}
}
