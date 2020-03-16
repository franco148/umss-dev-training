package com.umss.dev.training.jtemplate.event.handler.filter;

import com.umss.dev.training.jtemplate.service.JwtService;
import com.umss.dev.training.jtemplate.service.JwtServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtService jwtService;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtServiceImpl.HEADER_STRING);

        if (!requiresAuthentication(header)) {
            chain.doFilter(request, response);
            return;
        }


        UsernamePasswordAuthenticationToken authentication = null;
        if (jwtService.validate(header)) {

            authentication = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header), null, jwtService.getRoles(header));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private boolean requiresAuthentication(String header) {
        return header != null && header.startsWith("Bearer ");
    }
}
