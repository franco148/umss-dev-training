package com.umss.dev.training.jtemplate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 1. FIRST VERSION
        auth.inMemoryAuthentication()
                .withUser("franco").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}password").roles("USER", "ADMIN");
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
                .anyRequest().authenticated();
    }
}
