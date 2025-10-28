package vn.ute.utescore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Guest_SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 🚪 1️⃣ CẤP QUYỀN TRUY CẬP
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/home", "/register", "/login",
                    "/pitches/**", "/news/**", "/promotions/**",
                    "/css/**", "/js/**", "/images/**", "/api/**"
                ).permitAll() // ✅ Cho phép khách truy cập
                .requestMatchers("/customer/**", "/employee/**", "/admin/**", "/book/**", "/profile/**")
                .permitAll() // ⚠️ Cho phép tạm thời để test (không chặn login)
                .anyRequest().permitAll()
            )

            // 🔒 2️⃣ TẮT TOÀN BỘ CƠ CHẾ MẶC ĐỊNH
            .csrf(csrf -> csrf.disable())        // Không cần CSRF khi dùng form thủ công
            .formLogin(form -> form.disable())   // Không dùng form login mặc định
            .logout(logout -> logout.disable())  // Không dùng logout mặc định
            .httpBasic(httpBasic -> httpBasic.disable()); // Tắt HTTP Basic Auth (user/password popup)

        return http.build();
    }

    // 🔐 3️⃣ Mã hoá mật khẩu bằng BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
