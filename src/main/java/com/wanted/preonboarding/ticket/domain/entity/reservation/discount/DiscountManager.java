package com.wanted.preonboarding.ticket.domain.entity.reservation.discount;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class DiscountManager {

    private final List<DiscountPolicy> discounts;

    private DiscountManager() {
        this.discounts = List.of(
                new NewYearDiscount()
        );
    }

    public DiscountPolicy findDiscountOf(int code) {
        return discounts.stream()
                .filter(discount -> discount.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("유효한 할인 정책을 찾을 수 없습니다."));
    }
}
