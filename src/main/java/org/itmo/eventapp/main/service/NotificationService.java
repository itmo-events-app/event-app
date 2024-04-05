package org.itmo.eventapp.main.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.entity.Notification;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.repository.NotificationRepository;
import org.itmo.eventapp.main.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void createNotification(String title, String description, Integer userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found!(" + userId + ")"));

        notificationRepository.save(Notification.builder()
                .user(user)
                .title(title)
                .description(description)
                .seen(false)
                .sentTime(LocalDateTime.now())
                .build());
    }

    public Notification updateToSeen(Integer notificationId, Integer userId) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.NOTIFICATION_ERROR_MESSAGE));

        if (!notification.getUser().getId().equals(userId)) {
            // abort operation if user id mismatch
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.NOTIFICATION_ERROR_MESSAGE);
        }
        notification.setSeen(true);
        notification.setSentTime(LocalDateTime.now());
        notificationRepository.save(notification);
        return notification;
    }

    public List<Notification> updateSeenToAllByUserId(@NotNull Integer userId, Integer page, Integer size) {
        notificationRepository.updateAllSeenByUserId(userId);
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("sentTime").descending());
        return notificationRepository.getAllByUserId(userId, pageRequest);
    }

    public List<Notification> getAllByUserId(@NotNull Integer userId, Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("sentTime").descending());
        return notificationRepository.getAllByUserId(userId, pageRequest);
    }

    public void deleteNotification(Integer notificationId) {
        if (notificationRepository.existsById(notificationId)) {
            notificationRepository.deleteById(notificationId);
        } else {
            throw new EntityNotFoundException("Уведомление с заданным ID не найдено!(" + notificationId + ")");
        }
    }
}
