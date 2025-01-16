package com.spring.springoauth2.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        // 추후 수정하자 => OAuth2USer를 그대로 리턴하는 것은 좋은 선택이 아님
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }
}
