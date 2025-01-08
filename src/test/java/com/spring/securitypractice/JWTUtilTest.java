package com.spring.securitypractice;

import com.spring.securitypractice.domain.jwt.JWTUtil;
import com.spring.securitypractice.domain.user.data.dao.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class JWTUtilTest {

    private static final Logger log = LoggerFactory.getLogger(JWTUtilTest.class);

    @Autowired
    private JWTUtil jwtUtil;
    
    @Test
    @DisplayName("토큰 생성 테스트")
    public void createAccessTokenTest(){
        // given
        User testUser = User.builder()
                .id(1L)
                .userId("user_admin")
                .password("testAdmin")
                .userName("테스트")
                .roles(List.of("ROLE_ADMIN"))
                .build();

        // when
        String token = jwtUtil.createAccessToken(testUser);
        log.info("[createAccessTokenTest] access token 생성 결과 : {}", token);

        // then
        Assertions.assertThat(token).isNotNull();
    }

    @Test
    public void getUserIdByTokenTest(){
        String userId = "test_user";

        // given
        User testUser2 = User.builder()
                .id(1L)
                .userId(userId)
                .password("testAdmin")
                .userName("테스트")
                .roles(List.of("ROLE_ADMIN"))
                .build();

        // when
        String token = jwtUtil.createAccessToken(testUser2);
        log.info("[createAccessToken] access token 생성 결과 : {}", token);

        String testUserId = jwtUtil.getUserIdByToken(token);
        log.info("[getUserIdByTokenTest] 찾은 userId : {}", testUserId);

        // then
        Assertions.assertThat(testUserId).isEqualTo(userId);
    }
}
