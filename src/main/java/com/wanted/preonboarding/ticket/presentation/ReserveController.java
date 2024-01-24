package com.wanted.preonboarding.ticket.presentation;

import com.wanted.preonboarding.ticket.application.TicketSeller;
import com.wanted.preonboarding.ticket.domain.dto.request.ReserveInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reserve")
@RequiredArgsConstructor
public class ReserveController {
    private final TicketSeller ticketSeller;

    @PostMapping("/")
    public boolean reservation(@RequestBody ReserveInfo reserveInfo) {
        System.out.println("reservation");

        return ticketSeller.reserve(ReserveInfo.builder()
            .performanceId(reserveInfo.getPerformanceId())
            .reservationName(reserveInfo.getReservationName())
            .reservationPhoneNumber(reserveInfo.getReservationPhoneNumber())
            .reservationStatus(reserveInfo.getReservationStatus())
            .amount(reserveInfo.getAmount())
            .round(reserveInfo.getRound())
            .line(reserveInfo.getLine())
            .seat(reserveInfo.getSeat())
            .build()
        );
    }
}
