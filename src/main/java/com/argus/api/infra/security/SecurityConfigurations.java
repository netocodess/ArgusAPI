package com.argus.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/cadastro").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        // <-- CONDOMINIO -- > //
                        .requestMatchers(HttpMethod.POST, "/condominio").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/condominio/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/condominio").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/condominio/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/condominio/{id}").hasRole("ADMIN")
                        // <-- USUARIOS -- > //
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        // <-- COMUNICADO -- > //
                        .requestMatchers(HttpMethod.POST, "/comunicado").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.PUT, "/comunicado/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.DELETE, "/comunicado/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        // <-- AREAS COMUNS -- > //
                        .requestMatchers(HttpMethod.POST, "/areasComuns").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.PUT, "/areasComuns/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.DELETE, "/areasComuns/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        // <-- SESSAO VOTACAO -- > //
                        .requestMatchers(HttpMethod.POST, "/sessaoVotacao").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.DELETE, "/sessaoVotacao/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        // <-- VOTOS -- > //
                        .requestMatchers(HttpMethod.GET, "/votos").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.GET, "/votos/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.DELETE, "/votos/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        // <-- OCORRENCIAS -- > //
                        .requestMatchers(HttpMethod.POST, "/ocorrencias").hasRole("MORADOR")
                        .requestMatchers(HttpMethod.GET, "/ocorrencias").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.GET, "/ocorrencias/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.PUT, "/ocorrencias/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .requestMatchers(HttpMethod.DELETE, "/ocorrencias/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        // <-- RESERVAS  -- > //
                        .requestMatchers(HttpMethod.DELETE, "/reservas/{id}").hasAnyRole("ADMIN", "SINDICO", "SUBSINDICO")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://argus-pwa.vercel.app/"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
