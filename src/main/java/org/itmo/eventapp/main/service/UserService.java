package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.dto.request.*;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.model.entity.enums.LoginType;
import org.itmo.eventapp.main.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserLoginInfoService userLoginInfoService;
    private final UserNotificationInfoService userNotificationInfoService;
    private final UserPasswordRecoveryInfoService userPasswordRecoveryInfoService;

    private final AuthenticationManager authenticationManager;

    public User findById(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.USER_NOT_FOUND_MESSAGE));
    }

    public boolean existsByRolesId(Integer roleId) {
        return userRepository.existsByRolesId(roleId);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByLogin(String login) {
        return userRepository.findByUserLoginInfo_Login(login)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.USER_NOT_FOUND_MESSAGE));
    }

    public void updateNotifications(String login, NotificationSettingsRequest request) {
        User user = userLoginInfoService.findByLogin(login).getUser();
        UserNotificationInfo userNotificationInfo = user.getUserNotificationInfo();
        userNotificationInfo.setEnableEmailNotifications(request.enableEmail());
        userNotificationInfo.setEnablePushNotifications(request.enablePush());
        userNotificationInfoService.save(userNotificationInfo);
    }

    public void changeName(String login, UserChangeNameRequest request) {
        User user = userLoginInfoService.findByLogin(login).getUser();
        user.setName(request.name());
        user.setSurname(request.surname());
        userRepository.save(user);
    }

    //TODO добавить нотификацию о смене почты и его подтверждения
    public void changeLogin(String login, UserChangeLoginRequest request) {
        User user = userLoginInfoService.findByLogin(login).getUser();

        // Проверка на уникальность нового email перед обновлением
        if (userLoginInfoService.existsByLogin(request.login())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionConst.USER_EMAIL_EXIST);
        }

        if (request.type() == LoginType.EMAIL) {
            userLoginInfoService.setEmail(user.getUserLoginInfo(), request.login());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.INVALID_LOGIN_TYPE);
        }
    }

    public void changePassword(String login, UserChangePasswordRequest request) {
        User user = userLoginInfoService.findByLogin(login).getUser();

        String oldPassword = request.oldPassword();
        String newPassword = request.newPassword();
        String cnfPassword = request.confirmNewPassword();

        // Проверяем, что новый пароль совпадает с подтверждением
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.USER_PASSWORD_MISMATCH_MESSAGE);
        }

        // Проверяем, что текущий и новый пароль не совпадают
        if (newPassword.equals(oldPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.USER_PASSWORD_UNCHANGED_MESSAGE);
        }

        // Проверяем, что верно введен старый пароль
        try {
            var authentication = new UsernamePasswordAuthenticationToken(login, oldPassword);
            authenticationManager.authenticate(authentication);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.USER_AUTHENTICATION_FAIL_MESSAGE);
        }

        userLoginInfoService.setPassword(user.getUserLoginInfo(), request.newPassword());
    }

    public void changePassword(NewPasswordRequest request) {
        UserPasswordRecoveryInfo info = userPasswordRecoveryInfoService.findByToken(request.token());

        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.USER_PASSWORD_MISMATCH_MESSAGE);
        }

        userLoginInfoService.setPassword(info.getUser().getUserLoginInfo(), request.newPassword());
    }

    public Set<Privilege> getUserSystemPrivileges(Integer userId) {
        User user = findById(userId);
        return user.getRoles().stream()
                .map(Role::getPrivileges)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public Page<User> getAllFilteredUsers(String searchQuery, Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        if (searchQuery.isEmpty()) {
            return userRepository.findAll(pageRequest);
        } else {
            String rightPart;
            String leftPart;
            String[] parts = searchQuery.split(" ", 2);
            rightPart = parts[0];
            if (parts.length == 2) {
                leftPart = parts[1];
            } else {
                leftPart = parts[0];
            }
            return userRepository.findByFullName(rightPart, leftPart, pageRequest);
        }
    }
}
