package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.dto.request.NotificationSettingsRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangeLoginRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangeNameRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangePasswordRequest;
import org.itmo.eventapp.main.model.dto.response.ProfileResponse;
import org.itmo.eventapp.main.model.dto.response.UserInfoResponse;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.UserNotificationInfo;
import org.itmo.eventapp.main.model.entity.enums.LoginType;
import org.itmo.eventapp.main.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserLoginInfoService userLoginInfoService;
    private final UserNotificationInfoService userNotificationInfoService;

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.USER_NOT_FOUND_MESSAGE));
    }

    public boolean existsByRole(Role role) {
        return userRepository.existsByRole(role);
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

    public ProfileResponse getUserInfo(String login){
        User user = userLoginInfoService.findByLogin(login).getUser();
        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                Collections.singletonList(new UserInfoResponse(login, user.getUserLoginInfo().getLoginType())),
                user.getUserLoginInfo().getLastLoginDate(),
                user.getUserNotificationInfo().isEnablePushNotifications(),
                user.getUserNotificationInfo().isEnableEmailNotifications(),
                user.getUserNotificationInfo().getDevices()
        );
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.INVALID_TYPE);
        }
    }

    public void changePassword(String login, UserChangePasswordRequest request) {
        User user = userLoginInfoService.findByLogin(login).getUser();

        // Проверяем, что новый пароль совпадает с подтверждением
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.USER_PASSWORD_MISMATCH);
        }

        userLoginInfoService.setPassword(user.getUserLoginInfo(), request.newPassword());
    }

    public Set<Privilege> getUserSystemPrivileges(Integer userId) {
        User user = findById(userId);
        return user.getRole().getPrivileges();
    }
}
