package vn.ute.utescore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class Guest_SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // ✅ Cho phép guest truy cập tự do các trang công khai
                .requestMatchers(
                    "/", "/home", "/pitches", "/pitches/**",
                    "/news", "/news/**",
                    "/promotions", "/promotions/**",
                    "/register", "/login",
                    "/images/**", "/css/**", "/js/**"
                ).permitAll()

                // 🔒 Các trang riêng (VD: đặt sân, profile, quản lý)
                .requestMatchers("/book/**", "/profile/**", "/admin/**", "/customer/**").authenticated()

                // ⚙️ Mọi request khác vẫn cho phép truy cập
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
