package com.wanted.preonboarding.ticket.application;

import com.wanted.preonboarding.ticket.domain.dto.request.IsReserveOption;
import com.wanted.preonboarding.ticket.domain.dto.request.ReservationInfo;
import com.wanted.preonboarding.ticket.domain.dto.request.ReserveInfo;
import com.wanted.preonboarding.ticket.domain.dto.response.DetailReserveInfo;
import com.wanted.preonboarding.ticket.domain.dto.response.PerformanceInfo;
import com.wanted.preonboarding.ticket.domain.entity.performance.Performance;
import com.wanted.preonboarding.ticket.domain.entity.reservation.Reservation;
import com.wanted.preonboarding.ticket.domain.entity.reservation.discount.DiscountManager;
import com.wanted.preonboarding.ticket.domain.entity.reservation.discount.DiscountPolicy;
import com.wanted.preonboarding.ticket.infrastructure.repository.PerformanceRepository;
import com.wanted.preonboarding.ticket.infrastructure.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketSeller {
    private final PerformanceRepository performanceRepository;
    private final ReservationRepository reservationRepository;
    private final DiscountManager discountManager;
    private long totalAmount = 0L;

    public List<PerformanceInfo> getAllPerformanceInfoList(IsReserveOption isReserveOption) {
        return performanceRepository.findByIsReserve(isReserveOption.getIsReserve())
            .stream()
            .map(PerformanceInfo::of)
            .toList();
    }

    public PerformanceInfo getPerformanceInfoDetail(String name) {
        return PerformanceInfo.of(performanceRepository.findByName(name));
    }

    public boolean reserve(ReserveInfo reserveInfo) {
        log.info("reserveInfo ID => {}", reserveInfo.getPerformanceId());
        Performance info = performanceRepository.findById(reserveInfo.getPerformanceId())
            .orElseThrow(EntityNotFoundException::new);
        String enableReserve = info.getIsReserve();
        if (enableReserve.equalsIgnoreCase("enable")) {
            // 1-1. 결제
            int price = info.getPrice();
            // 1-2. 할인
            DiscountPolicy discountPolicy = discountManager.findDiscountOf(reserveInfo.getDiscountType());
            price -= discountPolicy.discount(price);
            reserveInfo.setAmount(reserveInfo.getAmount() - price);
            // 2. 예매 진행
            reservationRepository.save(Reservation.of(reserveInfo));
            return true;

        } else {
            return false;
        }
    }

    public DetailReserveInfo getReserveInfoDetail(ReservationInfo reservationInfo) {
        Reservation reservation = reservationRepository.findByNameAndPhoneNumber(
                reservationInfo.getReservationName(), reservationInfo.getReservationPhoneNumber());
        DetailReserveInfo reservedPerformance = DetailReserveInfo.of(reservation);

        String performanceName = performanceRepository.findById(reservation.getPerformanceId())
                .orElseThrow(EntityNotFoundException::new).getName();
        DetailReserveInfo.builder()
                .performanceName(performanceName)
                .build();

        return reservedPerformance;
    }

}
