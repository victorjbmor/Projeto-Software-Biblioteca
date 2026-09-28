package com.example.demo.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
public class SecurityConfig {

	private final SecretKey chave;

	public SecurityConfig(@Value("${jwt.secret}") String secret) {
		// HS256 exige uma chave de pelo menos 256 bits (32 bytes)
		this.chave = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// API stateless com token no header: nao ha cookie de sessao para
			// um ataque CSRF explorar, entao a protecao pode ser desligada
			.csrf(csrf -> csrf.disable())
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// Permite o console do H2 (que usa frames) em desenvolvimento
			.headers(h -> h.frameOptions(f -> f.sameOrigin()))
			// As regras sao avaliadas de cima para baixo: a primeira que casar vence
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/registrar", "/auth/login").permitAll()
				.requestMatchers("/h2-console/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/livros/**", "/autores/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/emprestimos").authenticated()
				.requestMatchers(HttpMethod.GET, "/emprestimos/meus").authenticated()
				.requestMatchers("/autores/**", "/livros/**", "/usuarios/**", "/emprestimos/**").hasRole("ADMIN")
				.anyRequest().authenticated())
			// Liga o filtro que le "Authorization: Bearer <token>" e valida o JWT
			.oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()));
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Usado para ASSINAR tokens (no login)
	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(chave));
	}

	// Usado para VALIDAR tokens (em toda requisicao protegida):
	// confere assinatura e expiracao
	@Bean
	public JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withSecretKey(chave).macAlgorithm(MacAlgorithm.HS256).build();
	}

	// Transforma a claim "roles" do token em permissoes do Spring (ROLE_ADMIN, ROLE_USER),
	// que e o que o hasRole("ADMIN") acima confere
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
		authorities.setAuthoritiesClaimName("roles");
		authorities.setAuthorityPrefix("ROLE_");

		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(authorities);
		return converter;
	}
}
