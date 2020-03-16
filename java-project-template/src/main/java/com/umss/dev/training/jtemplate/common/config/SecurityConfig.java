package com.umss.dev.training.jtemplate.common.config;

import com.umss.dev.training.jtemplate.event.handler.filter.JwtAuthenticationFilter;
import com.umss.dev.training.jtemplate.event.handler.filter.JwtAuthorizationFilter;
import com.umss.dev.training.jtemplate.service.JwtService;
import com.umss.dev.training.jtemplate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
//@EnableWebSecurity // Remove the annotation as it is already done by SpringBoot.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtService jwtService;


    public SecurityConfig(UserService userService, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 1. FIRST VERSION
//        auth.inMemoryAuthentication()
//                .withUser("franco").password("{noop}password").roles("USER")
//                .and()
//                .withUser("admin").password("{noop}password").roles("USER", "ADMIN");

        // 2. SECOND VERSION
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 1. FIRST VERSION
//        http.authorizeRequests().antMatchers("/", "/h2-console/**").permitAll()
//                .anyRequest().authenticated();
//        http.csrf().disable();
//        http.headers().frameOptions().disable();

        // 2. SECOND VERSION
        http.authorizeRequests().antMatchers("/", "/h2-console/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/users").permitAll() // This can be added directly to the endpoint with @PermitAll annotation.
//                .anyRequest().authenticated() // If this is still enabled, @PermitAll is not going to work
                .and()
                    .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtService))
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
    }

    // https://stackoverflow.com/questions/48822278/spring-5-no-servletcontext-set-exception
//    @Bean
//    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
//        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
//    }
}
