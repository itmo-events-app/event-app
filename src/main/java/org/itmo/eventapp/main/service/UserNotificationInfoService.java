package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.UserNotificationInfo;
import org.itmo.eventapp.main.repository.UserNotificationInfoRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserNotificationInfoService {

    private final UserNotificationInfoRepository userNotificationInfoRepository;

    public void save(UserNotificationInfo userNotificationInfo) {
        userNotificationInfoRepository.save(userNotificationInfo);
    }
}
