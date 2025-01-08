package com.spring.securitypractice.global.filter;

import com.spring.securitypractice.domain.jwt.JWTUtil;
import com.spring.securitypractice.domain.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
*
*   JWT 인증시 사용하는 필터입니다.
*
*/

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        
        // header에 token이 있는 경우
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            // JWT 유효성 검증
            if(jwtUtil.validateToken(token)) {
                String userId = jwtUtil.getUserIdByToken(token);

                // token에서 꺼낸 정보로 user찾기
                UserDetails user = userService.loadUserByUsername(userId);
                
                if(user != null) {
                    // UsernamePasswordAuthenticationToken에 접근 권한 요청 -> Role 생성
                    // 여기서 찾아낸 role을 통해서 접근 권한 제어할듯
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    // spring security에 인증 권한 설정하기
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);    // 다음 필터로 넘기기
    }
}
