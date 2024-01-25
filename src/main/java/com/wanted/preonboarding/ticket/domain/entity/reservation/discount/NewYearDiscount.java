package com.wanted.preonboarding.ticket.domain.entity.reservation.discount;

public class NewYearDiscount implements DiscountPolicy {
    @Override
    public int discount(int price) {
        return (int) (price * 0.8);
    }
}
