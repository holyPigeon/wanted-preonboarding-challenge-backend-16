package com.wanted.preonboarding.ticket.domain.entity.reservation.discount;

public class NewYearDiscount implements DiscountPolicy {
    private final String name = "NEW_YEAR";
    private final int code = 1;

    @Override
    public int discount(int price) {
        return (int) (price * 0.8);
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }
}
