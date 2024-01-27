package com.wanted.preonboarding.ticket.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class PerformanceCancelNotification {
    private String title;
    private String content;
    private UUID performanceId;
    private String performanceName;
    private int round;
    private Date startDate;
    private String isReserve;
}
