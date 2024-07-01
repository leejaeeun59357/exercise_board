package org.board.exercise_board.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.board.exercise_board.notification.service.NotificationService;
import org.board.exercise_board.user.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "SSE 연결")
    @GetMapping("/subscribe")
    public ResponseEntity<SseEmitter> subscribe(
            @AuthenticationPrincipal User user
    ) {
        SseEmitter sseEmitter = notificationService.subscribe(user.getLoginId());
        return ResponseEntity.ok(sseEmitter);
    }
}
