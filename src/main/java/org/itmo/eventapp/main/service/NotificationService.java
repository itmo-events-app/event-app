package org.itmo.eventapp.main.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.Notification;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.repository.NotificationRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

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
                .readTime(null)
                .build());
    }

    public void updateToSeen(Integer notificationId) {

        notificationRepository
                .findById(notificationId)
                .ifPresentOrElse(
                        (notification) -> {
                            notification.setSeen(true);
                            notification.setReadTime(LocalDateTime.now());
                            notificationRepository.save(notification);
                        },
                        () ->
                        {
                            //todo abort operation if not exist
                            throw new EntityNotFoundException("Уведомление с заданным ID не найдено!(" + notificationId + ")");
                        }
                );
    }

    public void updateSeenToAllByUserId(@NotNull User user) {
        notificationRepository.updateAllSeenByUserId(user.getId());
    }

    public List<Notification> getAllByUserId(@NotNull User user) {
        return notificationRepository.getAllByUserId(user.getId());
    }

    public void deleteNotification(Integer notificationId) {
        if (notificationRepository.existsById(notificationId)) {
            notificationRepository.deleteById(notificationId);
        } else {
            throw new EntityNotFoundException("Уведомление с заданным ID не найдено!(" + notificationId + ")");
        }
    }
}
