package com.hello.core.discount;

import com.hello.core.member.Grade;
import com.hello.core.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RateDiscountPolicyTest {

    RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인을 적용 받아야한다.")
    public void vip_o(){
        // given
        Member member = new Member(1L, "test", Grade.VIP);
        // when
        int discountPrice = rateDiscountPolicy.discount(member, 10000);
        // then
        assertThat(discountPrice).isEqualTo(1000);
    }

    // test코드는 성공시 말고 실패시 test도 작성한다.

    @Test
    @DisplayName("VIP가 아닌 경우에는 할인이 적용되면 안된다.")
    public void vip_x(){
        // given
        Member member = new Member(1L, "test", Grade.BASIC);
        // when
        int discountPrice = rateDiscountPolicy.discount(member, 10000);
        // then
        assertThat(discountPrice).isEqualTo(0);
    }
}