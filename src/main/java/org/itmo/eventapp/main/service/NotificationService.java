package org.itmo.eventapp.main.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.response.NotificationResponse;
import org.itmo.eventapp.main.model.entity.Notification;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.repository.NotificationRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepo;

    public Notification createNotification(String title, String description, @NotNull User user){
        //todo add exception throw if user not exist somehow

        Notification n = Notification.builder()
                .user(user)
                .title(title)
                .description(description)
                .seen(false)
                .readTime(null)
                .build();

        notificationRepo.save(n);

        return n;
    }

    public Notification updateToSeen(Integer notificationId){
        Notification n;
        Optional<Notification> opt = notificationRepo.findById(notificationId);

        if (opt.isPresent()){
            n = opt.get();
        } else {
            //abort operation if not exist
            throw new EntityNotFoundException("Уведомление с заданным ID не найдено!("+notificationId+")");
        }

        n.setSeen(true);
        n.setReadTime(LocalDateTime.now());

        notificationRepo.save(n);
        return n;
    }

    public void updateSeenToAllByUserId(@NotNull User user){
        notificationRepo.updateAllSeenByUser_Id(user.getId());
    }

    public List<Notification> getAllByUserId(@NotNull User user){
        return notificationRepo.getAllByUser_Id(user.getId());
    }

    public Boolean deleteNotification(Integer id){
        if (notificationRepo.existsById(id)) {
            notificationRepo.deleteById(id);
            return true;
        } else {
            return false;
        }

    }
}
