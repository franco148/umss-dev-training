package com.umss.dev.training.jtemplate.event.handler.filter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umss.dev.training.jtemplate.service.JwtService;
import com.umss.dev.training.jtemplate.service.JwtServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        // by default the interceptor is /login, so we can override it as follows:
        // setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
        this.jwtService = jwtService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);// or request.getParameter(username);
        String password = obtainPassword(request);// or request.getParameter(password);

        if (username != null && password != null) {
            logger.info("Username from request parameter (form-data): " + username);
            logger.info("Password from request parameter (form-data): " + password);
        } else {
            com.umss.dev.training.jtemplate.persistence.domain.User user = null;
            try {
                user = new ObjectMapper().readValue(request.getInputStream(), com.umss.dev.training.jtemplate.persistence.domain.User.class);

                username = user.getEmail();
                password = user.getPassword();

                logger.info("Username from request InputStream (raw): " + username);
                logger.info("Password from request InputStream (raw): " + password);
            } catch (JsonParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = jwtService.create(authResult);

        response.addHeader(JwtServiceImpl.HEADER_STRING, JwtServiceImpl.TOKEN_PREFIX.concat(token));

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("user", (User)authResult.getPrincipal());
        body.put("message", String.format("Hi %s, you have logged in successfully!", authResult.getName()));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Authentication error: invalid username or password");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");
    }
}
