package com.wanted.preonboarding.ticket.domain.entity.reservation.discount;

public interface DiscountPolicy {
    public int discount(int price);
    public int getCode();
    public String getName();
}
