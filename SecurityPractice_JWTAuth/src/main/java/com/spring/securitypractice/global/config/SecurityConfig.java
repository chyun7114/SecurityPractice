package com.spring.securitypractice.global.config;

import com.spring.securitypractice.domain.jwt.JWTUtil;
import com.spring.securitypractice.domain.user.data.dao.Role;
import com.spring.securitypractice.domain.user.service.UserService;
import com.spring.securitypractice.global.filter.JwtAuthFilter;
import com.spring.securitypractice.global.response.handler.CustomAccessDeniedHandler;
import com.spring.securitypractice.global.response.handler.CustomAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    // 각각 구현해야 함
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    // 관리자 권한을 가진 경우에만 접근 가능한 페이지
    private final String[] ADMIN_WHITE_LIST = {
            "/admin/**"
    };

    // 일반 유저 권한을 가진 경우에만 접근할 수 있는 페이지
    private final String[] USER_WHITE_LIST = {
            "/user/**"
    };

    // 지원자 권한을 가진 사람만 접근할 수 있는 페이지
    private final String[] APPLY_WHITE_LIST = {
            "/apply/**"
    };

    // 전체가 들어갈 수 있는 리스트
    // 근데 그냥 anyRequest.permitAll() 하면 될듯
    private final String[] AUTH_WHITE_LIST = {
            "/main/**", "/api/v1/member/**", "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html", "/api/v1/auth/**"
    };

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // csrf 비활성화
        http.csrf((csrf) -> csrf.disable());

        // cors 비활성화
        http.cors((cors) -> cors.disable());

        // 세션 상태 관리 없음으로 설정 -> Security는 세션을 생성하지 않는다.
        http.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)
        );

        // formLogin 비활성화
        http.formLogin(form -> form.disable());

        // httpBasic 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable);

        // JWT 권한 규칙 추가
        // 필터 앞에 추가하여 먼저 필터가 작동하도록 설정한다
        http.addFilterBefore(new JwtAuthFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );

        // 권한 규칙 추가
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers(ADMIN_WHITE_LIST).hasAnyRole(Role.ADMIN.name())
                        .requestMatchers(USER_WHITE_LIST).hasAnyRole(Role.USER.name())
                        .requestMatchers(APPLY_WHITE_LIST).hasAnyRole(Role.APPLY.name())
                        .requestMatchers(AUTH_WHITE_LIST).permitAll()
                        .anyRequest().permitAll()
        );

        return http.build();
    }
}
