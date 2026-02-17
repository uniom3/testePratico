package com.mendonca.testePratico.infrastructure.adapter.input.security;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenProvider {

    private final Map<String, String> tokenUserMap;

    public TokenProvider(SecurityProperties props) {
        this.tokenUserMap = props.getTokens();
        log.info("🔐 Tokens carregados via ConfigurationProperties: {}", tokenUserMap);
    }

    public boolean validateToken(String token) {
        return tokenUserMap.containsKey(token);
    }

    public String getUsernameFromToken(String token) {
        return tokenUserMap.get(token);
    }

    public List<SimpleGrantedAuthority> getAuthorities(String token) {
        String username = getUsernameFromToken(token);

        if ("envio".equals(username)) return List.of(new SimpleGrantedAuthority("ROLE_ENVIO"));
        if ("consulta".equals(username)) return List.of(new SimpleGrantedAuthority("ROLE_CONSULTA"));

        return List.of();
    }
}
