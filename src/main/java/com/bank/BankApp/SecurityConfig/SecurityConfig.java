package com.bank.BankApp.SecurityConfig;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. CORS Configuration attach kar rahe hain
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. CSRF ko disable kar rahe hain kyunki REST APIs me ye zaroori nahi hota (Stateful nahi hain)
            .csrf(csrf -> csrf.disable())
            
            // 3. Session Management ko STATELESS kar rahe hain (JWT standards ke liye)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 4. URL Endpoints Permissions
            .authorizeHttpRequests(auth -> auth
                // Login, Register aur Contact form ko bina kisi authentication ke allow karein
                .requestMatchers("/api/auth/**", "/api/contact/**").permitAll()
                
                // Baki saare banking transactions aur dashboard endpoints ke liye authentication mandatory hai
                // (Agar abhi JWT filter nahi lagaya hai, toh test karne ke liye temporary isko bhi .permitAll() kar sakte ho)
                .anyRequest().permitAll() 
            );

        return http.build();
    }

    // 5. Angular Frontend (Port 4200) ke liye CORS error fix karne ka sabse solid tareeka
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Angular app ka origin allow karein
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); 
        
        // Saare HTTP Methods allow karein
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Saare Request Headers allow karein (Authorization, Content-Type, etc.)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        
        // Credentials (Cookies/Auth headers) allow karein
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Pure project ke `/api/**` endpoints par is CORS policy ko apply karein
        source.registerCorsConfiguration("/api/**", configuration);
        
        return source;
    }
}



