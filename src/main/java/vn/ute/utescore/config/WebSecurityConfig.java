package vn.ute.utescore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Cho phép truy cập tất cả
            )
            .csrf(csrf -> csrf.disable()) // Tắt CSRF để không bị lỗi POST
            .formLogin(login -> login.disable()); // Tắt form login mặc định
        return http.build();
    }
}
