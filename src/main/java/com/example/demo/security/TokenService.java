package com.example.demo.security;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TokenResponseDTO;
import com.example.demo.model.Usuario;

@Service
public class TokenService {

	private final JwtEncoder jwtEncoder;
	private final Duration validade;

	public TokenService(JwtEncoder jwtEncoder, @Value("${jwt.expiracao-minutos}") long expiracaoMinutos) {
		this.jwtEncoder = jwtEncoder;
		this.validade = Duration.ofMinutes(expiracaoMinutos);
	}

	public TokenResponseDTO gerarToken(Usuario usuario) {
		Instant agora = Instant.now();
		Instant expiraEm = agora.plus(validade);

		// Claims = o "payload" do JWT. Qualquer um consegue LER isso
		// (e so Base64), entao nunca coloque dados sensiveis aqui.
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("biblioteca-api")
				.subject(usuario.getId().toString())
				.issuedAt(agora)
				.expiresAt(expiraEm)
				.claim("email", usuario.getEmail())
				.claim("roles", List.of(usuario.getRole().name()))
				.build();

		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
		return new TokenResponseDTO(token, "Bearer", expiraEm);
	}
}
