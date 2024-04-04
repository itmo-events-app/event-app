package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.dto.request.UserChangeEmailRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangeNameRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangePasswordRequest;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.repository.UserLoginInfoRepository;
import org.itmo.eventapp.main.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserLoginInfoService userLoginInfoService;

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.USER_NOT_FOUND_MESSAGE));
    }

    public List<User> findAllByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void changeName(String email, UserChangeNameRequest request) {
        User user = userLoginInfoService.findByEmail(email).getUser();
        user.setName(request.name());
        user.setSurname(request.surname());
        userRepository.save(user);
    }

    //TODO добавить нотификацию о смене почты и его подтверждения
    public void changeEmail(String email, UserChangeEmailRequest request) {
        User user = userLoginInfoService.findByEmail(email).getUser();

        // Проверка на уникальность нового email перед обновлением
        if (userLoginInfoService.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionConst.USER_EMAIL_EXIST);
        }

        userLoginInfoService.setEmail(user.getUserLoginInfo(), request.email());
    }

    public void changePassword(String email, UserChangePasswordRequest request) {
        User user = userLoginInfoService.findByEmail(email).getUser();

        // Проверяем, что новый пароль совпадает с подтверждением
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.USER_PASSWORD_MISMATCH);
        }

        userLoginInfoService.setPassword(user.getUserLoginInfo(), request.newPassword());
    }

}
