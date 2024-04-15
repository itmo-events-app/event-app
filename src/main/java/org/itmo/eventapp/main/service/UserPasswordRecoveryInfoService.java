package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.UserPasswordRecoveryInfo;
import org.itmo.eventapp.main.repository.UserPasswordRecoveryInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPasswordRecoveryInfoService implements IEmailTokenService{

    private final UserPasswordRecoveryInfoRepository userPasswordRecoveryInfoRepository;

    public UserPasswordRecoveryInfo findByToken(String token) {
        return userPasswordRecoveryInfoRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Токен не найден"));
    }

    @Override
    public String updateUserToken(User user) {
        String token = UUID.randomUUID().toString();

        UserPasswordRecoveryInfo info = UserPasswordRecoveryInfo.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();

        userPasswordRecoveryInfoRepository.save(info);

        return token;
    }
}
