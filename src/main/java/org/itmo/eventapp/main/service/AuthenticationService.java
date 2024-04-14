package org.itmo.eventapp.main.service;


import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.mail.MailSenderService;
import org.itmo.eventapp.main.model.dto.request.LoginRequest;
import org.itmo.eventapp.main.model.dto.request.RegistrationUserRequest;
import org.itmo.eventapp.main.model.dto.response.RegistrationRequestForAdmin;
import org.itmo.eventapp.main.model.entity.RegistrationRequest;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.model.entity.UserNotificationInfo;
import org.itmo.eventapp.main.model.entity.enums.LoginStatus;
import org.itmo.eventapp.main.model.entity.enums.LoginType;
import org.itmo.eventapp.main.model.entity.enums.RegistrationRequestStatus;
import org.itmo.eventapp.main.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final UserLoginInfoService userLoginInfoService;
    private final UserNotificationInfoService userNotificationInfoService;
    private final RoleService roleService;
    private final RegistrationRequestService registrationRequestService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private MailSenderService mailSenderService;

    public String login(LoginRequest loginRequest) {
        try {
            var authentication =
                new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password());
            authenticationManager.authenticate(authentication);

            var userLoginInfo = userLoginInfoService.findByLogin(loginRequest.login());
            userLoginInfoService.setLastLoginDate(userLoginInfo, LocalDateTime.now());

            return jwtTokenUtil.generateToken(loginRequest.login());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка аутентификации.");
        }
    }

    public void createRegisterRequest(RegistrationUserRequest registrationUserRequest) {
        String login = registrationUserRequest.login();

        if (registrationRequestService.existsByEmail(login)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionConst.REGISTRATION_REQUEST_EMAIL_EXIST);
        } else {
            RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .email(registrationUserRequest.login())
                .passwordHash(passwordEncoder.encode(registrationUserRequest.password()))
                .name(registrationUserRequest.name())
                .surname(registrationUserRequest.surname())
                .status(RegistrationRequestStatus.NEW)
                .sentTime(LocalDateTime.now())
                .build();
            registrationRequestService.save(registrationRequest);
        }
    }


    @Transactional
    public void approveRegistrationRequestCallback(int requestId) {

        var request = registrationRequestService.findById(requestId);

        if (request.getStatus() != RegistrationRequestStatus.NEW) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Запрос уже обработан");
        }

        var reader = roleService.getReaderRole();

        UserNotificationInfo notificationInfo = UserNotificationInfo.builder()
            .devices(new String[]{})
            .enableEmailNotifications(false)
            .enablePushNotifications(false)
            .build();

        userNotificationInfoService.save(notificationInfo);

        User user = User.builder()
            .name(request.getName())
            .surname(request.getSurname())
            .role(reader)
            .userNotificationInfo(notificationInfo)
            .build();

        userService.save(user);

        UserLoginInfo loginInfo = UserLoginInfo.builder()
            .registration(request)
            .login(request.getEmail())
            .loginType(LoginType.EMAIL)
            .passwordHash(request.getPasswordHash())
            .lastLoginDate(LocalDateTime.now())
            .user(user)
            .loginStatus(LoginStatus.APPROVED)
            .build();

        userLoginInfoService.save(loginInfo);

        request.setStatus(RegistrationRequestStatus.APPROVED);
        registrationRequestService.save(request);

        try {
            mailSenderService.sendApproveRegistrationRequestMessage(request.getEmail(), request.getName());
        } catch (MessagingException | IOException e) {
        }
    }

    @Transactional
    public void declineRegistrationRequestCallback(int requestId) {
        var request = registrationRequestService.findById(requestId);

        if (request.getStatus() != RegistrationRequestStatus.NEW) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Запрос уже обработан");
        }

        request.setStatus(RegistrationRequestStatus.DECLINED);
        registrationRequestService.save(request);

        try {
            mailSenderService.sendDeclineRegistrationRequestMessage(request.getEmail(), request.getName());
        } catch (MessagingException | IOException e) {
        }
    }

    public List<RegistrationRequestForAdmin> listRegisterRequestsCallback() {
        return registrationRequestService.getByStatus(RegistrationRequestStatus.NEW)
            .stream()
            .map((request) -> new RegistrationRequestForAdmin(
                request.getId(),
                request.getEmail(),
                request.getName(),
                request.getSurname(),
                request.getStatus(),
                request.getSentTime()))
            .toList();
    }
}
