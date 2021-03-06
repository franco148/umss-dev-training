package com.umss.dev.training.jtemplate.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umss.dev.training.jtemplate.event.handler.SimpleGrantedAuthorityMixin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @Value("#{new Long('${umss.dev.training.jwt.expiration-date}')}")
    private Long expirationTime;
    @Value("${umss.dev.training.jwt.token-secret}")
    private String tokenSecret;


    @Override
    public String create(Authentication auth) throws IOException {
        String username = auth.getName();
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

        Claims claims = Jwts.claims();
        claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
        String tokenBase64Bytes = Base64Utils.encodeToString(tokenSecret.getBytes());

        String token = Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS512, tokenBase64Bytes.getBytes())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();

        return token;
    }

    @Override
    public boolean validate(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {
        String tokenBase64Bytes = Base64Utils.encodeToString(tokenSecret.getBytes());
        Claims claims = Jwts.parser().setSigningKey(tokenBase64Bytes.getBytes())
                            .parseClaimsJws(resolve(token)).getBody();

        return claims;
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {

        Object roles = getClaims(token).get("authorities");

        //This is throwing an exception, because there is not a empty constructor for GrantedAuthority
        Collection<? extends GrantedAuthority> authorities = Arrays
                .asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
                        .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

        return authorities;
    }

    @Override
    public String resolve(String token) {
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");
        }
        return null;
    }
}
