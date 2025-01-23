package com.springboot.biz.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Configuration
@RequiredArgsConstructor
@Log4j2
public class CustomSecurityConfig {
 @Bean
 public SecurityFilterChain filter(HttpSecurity http) throws Exception {
     System.out.println("security filter chain");
     log.info("security filter chain 적용!!!!!!");
     http.cors(httpSecurityCorsConfigurer -> {
         httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
     });
     http.sessionManagement(SessionConfig -> SessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
     http.csrf(config -> config.disable());
     return http.build();
 }
 @Bean
 public CorsConfigurationSource corsConfigurationSource() {
     CorsConfiguration configuration = new CorsConfiguration();
     configuration.setAllowedOriginPatterns(Arrays.asList("*"));
     configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
     configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
     configuration.setAllowCredentials(true);
     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
     source.registerCorsConfiguration("/**", configuration);
     return source;
 }
 
 @Bean
 public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
 }
}