package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf
        http.csrf(csrf -> csrf.disable());

        // cors
        http.cors(cors -> cors.disable());

        // 기본 인증 비활성화
        http.httpBasic(httpBasic -> httpBasic.disable());

        // formLogin
        http.formLogin(formLogin -> formLogin.disable());

        return http.build();
    }
}
