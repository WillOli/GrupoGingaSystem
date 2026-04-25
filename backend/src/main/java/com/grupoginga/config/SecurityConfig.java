package com.grupoginga.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Libera explicitamente as pastas de recursos estáticos
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/static/**").permitAll()
                        // Libera o arquivo de login
                        .requestMatchers("/login.html").permitAll()
                        // Libera a raiz (caso você renomeie para index.html depois)
                        .requestMatchers("/").permitAll()
                        // Libera a API por enquanto
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}