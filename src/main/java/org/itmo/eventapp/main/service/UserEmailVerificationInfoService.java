package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.UserEmailVerificationInfo;
import org.itmo.eventapp.main.repository.UserEmailVerificationInfoRepository;
import org.itmo.eventapp.main.service.interfaces.IEmailTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserEmailVerificationInfoService implements IEmailTokenService<UserEmailVerificationInfo> {

    private final UserEmailVerificationInfoRepository userEmailVerificationInfoRepository;

    public UserEmailVerificationInfo findByToken(String token) {
        return userEmailVerificationInfoRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Токен не найден"));
    }

    public String updateUserToken(User user) {

        String token = UUID.randomUUID().toString();

        UserEmailVerificationInfo info = UserEmailVerificationInfo.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();

        userEmailVerificationInfoRepository.save(info);

        return token;
    }
}
