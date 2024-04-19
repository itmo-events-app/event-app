package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.entity.LoginAttempts;
import org.itmo.eventapp.main.repository.LoginAttemptsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginAttemptsService {

    private static final int LOCKOUT_TIME_MINUTES = 15;
    private static final int MAX_ATTEMPTS = 5;

    private final LoginAttemptsRepository loginAttemptsRepository;

    public LoginAttempts findByLogin(String login) {
        return loginAttemptsRepository.findByLogin(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.LOGIN_ATTEMPTS_NOT_FOUND));
    }

    private void blockUser(LoginAttempts attempts) {
        LocalDateTime expired = LocalDateTime.now().plusMinutes(LOCKOUT_TIME_MINUTES);
        attempts.setLockoutExpired(expired);
        loginAttemptsRepository.save(attempts);
    }

    public boolean checkIsUserNonBlocked(String login) {
        var attempts = findByLogin(login);

        return attempts.getAttempts() != MAX_ATTEMPTS
                || !attempts.getLockoutExpired().isAfter(LocalDateTime.now());
    }

    public void clearUserAttempts(String login) {
        LoginAttempts attempts = findByLogin(login);
        attempts.setAttempts(0);
        loginAttemptsRepository.save(attempts);
    }

    public boolean incrementUserAttempts(String login) {

        var attempts = findByLogin(login);

        if (attempts.getAttempts() == MAX_ATTEMPTS
                && attempts.getLockoutExpired().isBefore(LocalDateTime.now())) {
                blockUser(attempts);
                return false;
        }

        attempts.setAttempts(attempts.getAttempts() + 1);
        loginAttemptsRepository.save(attempts);

        return true;
    }

    public void save(LoginAttempts attempts) {
        loginAttemptsRepository.save(attempts);
    }
}
