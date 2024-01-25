package com.wanted.preonboarding.ticket.domain.dto.response;

import com.wanted.preonboarding.ticket.domain.entity.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DetailReserveInfo {
    private UUID performanceId;
    private String performanceName;
    private String reservationName;
    private String reservationPhoneNumber;
    private int round;
    private char line;
    private int seat;

    public static DetailReserveInfo of(Reservation entity) {
        return DetailReserveInfo.builder()
                .performanceId(entity.getPerformanceId())
                .reservationName(entity.getName())
                .reservationPhoneNumber(entity.getPhoneNumber())
                .round(entity.getRound())
                .line(entity.getLine())
                .seat(entity.getSeat())
                .build();
    }
}
