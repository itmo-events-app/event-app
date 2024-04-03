package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.repository.UserLoginInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Класс, отвечающий за обработку операций с данными о пользователя,
 * такими как почта и пароль.
 * Этот класс является вспомогательным для UserService, который содержит
 * операции с пользователями, изменение данных о пользователе
 * и управление его учетной записью.
 */
@RequiredArgsConstructor
@Service
public class UserLoginInfoService {
    private final UserLoginInfoRepository userLoginInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public UserLoginInfo findByEmail(String email){
        String test = email;
        return userLoginInfoRepository.getUserLoginInfoByEmail(email)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.USER_NOT_FOUND_MESSAGE));
    }

    public boolean existsByEmail(String email){
        return userLoginInfoRepository.existsByEmail(email);
    }

    public void setEmail(UserLoginInfo userLoginInfo, String email){
        userLoginInfo.setEmail(email);
    }

    public void setPassword(UserLoginInfo userLoginInfo, String password){
        userLoginInfo.setPasswordHash(passwordEncoder.encode(password));
    }

}
