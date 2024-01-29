package com.wanted.preonboarding.ticket.presentation;

import com.wanted.preonboarding.ticket.application.NotificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe/performance/{id}", produces = "text/event-stream")
    public SseEmitter subscribePerformance(
            @PathVariable Long id,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
            HttpServletResponse response) {
            return notificationService.subscribe(id, lastEventId, response);
    }
}
