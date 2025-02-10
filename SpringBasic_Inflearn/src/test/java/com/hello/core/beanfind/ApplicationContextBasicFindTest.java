package com.hello.core.beanfind;

import com.hello.core.AppConfig;
import com.hello.core.member.MemberService;
import com.hello.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextBasicFindTest {
    AnnotationConfigApplicationContext annotationConfigApplicationContext
            = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName(){
        MemberService memberService = annotationConfigApplicationContext.getBean("memberService", MemberService.class);
//        System.out.println("memberService = " + memberService);
//        System.out.println("memberService.getClass() = " + memberService.getClass());

        // 검증
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름 없이 타입으로 조회")
    void findBeanByType(){
        MemberService memberService = annotationConfigApplicationContext.getBean(MemberService.class);

        // 검증
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }


    // 역할을 조회해야지 구현을 조회하면 안된다
    // 조금 안좋은 코드일수도...?
    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByType2(){
        MemberServiceImpl memberService = annotationConfigApplicationContext.getBean("memberService", MemberServiceImpl.class);

        // 검증
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("빈 이름으로 조회 X")
    void findBeanByNameX(){
        // ac.getBean("xxxxx", MemberService.class)
        // 검증
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> annotationConfigApplicationContext.getBean("xxxxxx", MemberService.class));
    }
}
