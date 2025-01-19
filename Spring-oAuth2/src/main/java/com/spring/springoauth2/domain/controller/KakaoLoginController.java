package com.spring.springoauth2.domain.controller;

import com.spring.springoauth2.domain.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {
    private final KakaoLoginService kakaoLoginService;

    /**
     * 로그인 이후의 행동 규정함
     * @param code 로그인된 유저의 인증 코드
     * @return 인증이 올바르다면 ok를 리턴한다
     */
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam String code){
        String accessToken = kakaoLoginService.getAccessTokenFromKakao(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
