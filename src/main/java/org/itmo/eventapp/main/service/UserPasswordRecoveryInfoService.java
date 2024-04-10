package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.UserPasswordRecoveryInfo;
import org.itmo.eventapp.main.repository.UserPasswordRecoveryInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserPasswordRecoveryInfoService {

    private final UserPasswordRecoveryInfoRepository userPasswordRecoveryInfoRepository;

    public UserPasswordRecoveryInfo findByToken(String token) {
        return userPasswordRecoveryInfoRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Токен не найден"));
    }

    public void save(UserPasswordRecoveryInfo info) {
        userPasswordRecoveryInfoRepository.save(info);
    }
}
