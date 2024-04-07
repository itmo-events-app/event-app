package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.response.NotificationResponse;
import org.itmo.eventapp.main.model.entity.Notification;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.model.mapper.NotificationMapper;
import org.itmo.eventapp.main.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(
            @AuthenticationPrincipal UserLoginInfo userDetails,
            @RequestParam(name = "page") @Min(0) Integer page,
            @RequestParam(name = "size") @Min(1) @Max(25) Integer size
    ) {
        List<Notification> notifications = notificationService.getAllByUserId(userDetails.getUser().getId(), page, size);
        List<NotificationResponse> responseBody = new ArrayList<>();
        for (Notification notification: notifications) {
            responseBody.add(NotificationMapper.notificationToNotificationResponse(notification));
        }
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping
    public ResponseEntity<List<NotificationResponse>> setAllAsSeenNotifications(
            @AuthenticationPrincipal UserLoginInfo userDetails,
            @RequestParam(name = "page") @Min(0) Integer page,
            @RequestParam(name = "size") @Min(1) @Max(25) Integer size
    ) {

        List<Notification> notifications = notificationService.updateSeenToAllByUserId(userDetails.getUser().getId(), page, size);
        List<NotificationResponse> responseBody = new ArrayList<>();
        for (Notification notification: notifications) {
            responseBody.add(NotificationMapper.notificationToNotificationResponse(notification));
        }
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping(path = "/{notificationId}")
    public ResponseEntity<NotificationResponse> setOneAsSeenNotification(
            @AuthenticationPrincipal UserLoginInfo userDetails,
            @Valid @PathVariable Integer notificationId
    ) {
        NotificationResponse responseBody = NotificationMapper
                .notificationToNotificationResponse(
                        notificationService.updateToSeen(
                            notificationId,
                            userDetails.getUser().getId()));
        return ResponseEntity.ok(responseBody);
    }

}