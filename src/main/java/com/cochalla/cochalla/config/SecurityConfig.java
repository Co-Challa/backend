package com.cochalla.cochalla.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cochalla.cochalla.security.JwtAuthenticationFilter;
import com.cochalla.cochalla.security.JwtUtil;
import com.cochalla.cochalla.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtFilter,
            CustomUserDetailService userDetailService) throws Exception {
        http.csrf().disable()
                .cors().and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup", "/signin", "/post/*", "/comment/list").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtFilter, userDetailService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ CORS 필터 등록
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setExposedHeaders(List.of("Authorization"));
        
        String frontendCloudtypeUrl = frontendUrl;
        if (frontendCloudtypeUrl != null && !frontendCloudtypeUrl.isEmpty()) {
            config.addAllowedOrigin(frontendCloudtypeUrl);
        }

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
