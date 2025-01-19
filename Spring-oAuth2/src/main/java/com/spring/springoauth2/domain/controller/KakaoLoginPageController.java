package com.spring.springoauth2.domain.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class KakaoLoginPageController {

    @Value("${kakao.client_id}")
    private String kakaoClientId;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.login_uri}")
    private String kakaoLoginUri;

    /**
     * 카카오 로그인 페이지로 넘어가기 위한 매핑이다.
     * @param model thymeleaf 내 location에 대응되기 위한 uri
     * @return login.html
     */

    @GetMapping("/page")
    public String kakaoLoginPage(Model model) {
        String location = new StringBuilder()
                .append(kakaoLoginUri)
                .append("?response_type=code&client_id=")
                .append(kakaoClientId)
                .append("&redirect_uri=")
                .append(kakaoRedirectUri)
                .toString();
        model.addAttribute("location", location);

        return "login";
    }
}
