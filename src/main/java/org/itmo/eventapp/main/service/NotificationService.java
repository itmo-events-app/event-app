package org.itmo.eventapp.main.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.entity.Notification;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.repository.NotificationRepository;
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

    public void createNotification(String title, String description, @NotNull User user) {
        //todo add exception throw if user not exist somehow

        notificationRepository.save(Notification.builder()
                .user(user)
                .title(title)
                .description(description)
                .seen(false)
                .sentTime(null)
                .build());
    }

    public Notification updateToSeen(Integer notificationId, User user) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.NOTIFICATION_ERROR_MESSAGE));

        if (!notification.getUser().getId().equals(user.getId())) {
            // abort operation if user id mismatch
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.NOTIFICATION_ERROR_MESSAGE);
        }
        notification.setSeen(true);
        notification.setSentTime(LocalDateTime.now());
        notificationRepository.save(notification);
        return notification;
    }

    public List<Notification> updateSeenToAllByUserId(@NotNull User user, Integer page, Integer size) {
        notificationRepository.updateAllSeenByUserId(user.getId());
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("sent_time").descending());

        return notificationRepository.getAllByUserId(user.getId(), pageRequest);
    }

    public List<Notification> getAllByUserId(@NotNull User user, Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("sent_time").descending());
        return notificationRepository.getAllByUserId(user.getId(), pageRequest);
    }

    public void deleteNotification(Integer notificationId) {
        if (notificationRepository.existsById(notificationId)) {
            notificationRepository.deleteById(notificationId);
        } else {
            throw new EntityNotFoundException("Уведомление с заданным ID не найдено!(" + notificationId + ")");
        }
    }
}
