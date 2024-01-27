package com.wanted.preonboarding.ticket.application;

import com.wanted.preonboarding.ticket.domain.dto.response.PerformanceCancelNotification;
import com.wanted.preonboarding.ticket.domain.entity.performance.Performance;
import com.wanted.preonboarding.ticket.infrastructure.repository.EmitterRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;

    public SseEmitter subscribe(Long userId, String lastEventId, HttpServletResponse response) {
        String id = userId + "_" + System.currentTimeMillis();

        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));
        response.setHeader("X-Accel-Buffering", "no");

        emitter.onCompletion(() -> emitterRepository.deleteAllStartWithId(id));
        emitter.onTimeout(() -> emitterRepository.deleteAllStartWithId(id));
        emitter.onError(error -> emitterRepository.deleteAllStartWithId(id));

        sendToClient(emitter, id, "EventStream Created. [userId=" + id + "]");

        if (!lastEventId.isEmpty()) {
            Map<String, SseEmitter> events = emitterRepository.findAllStartById(id);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    public void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteAllStartWithId(id);
            log.error("SSE 연결 오류 발생", e);
        }
    }

    public void send(String title, String content, Performance performance) {
        String id = "user";
        PerformanceCancelNotification performanceCancelNotification = createNotification(id, title, content, performance);

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, performanceCancelNotification);
                    // 데이터 전송
                    sendToClient(emitter, key, performanceCancelNotification);
                }
        );
    }

    private PerformanceCancelNotification createNotification(String id, String title, String content, Performance performance) {
        return PerformanceCancelNotification.builder()
                .title(title)
                .content(content)
                .performanceId(performance.getId())
                .performanceName(performance.getName())
                .round(performance.getRound())
                .startDate(performance.getStart_date())
                .isReserve(performance.getIsReserve())
                .build();
    }
}
