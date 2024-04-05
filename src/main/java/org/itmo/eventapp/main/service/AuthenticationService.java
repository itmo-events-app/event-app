package org.itmo.eventapp.main.service;


import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.mail.MailSenderService;
import org.itmo.eventapp.main.model.dto.request.LoginRequest;
import org.itmo.eventapp.main.model.dto.request.RegistrationUserRequest;
import org.itmo.eventapp.main.model.dto.response.RegistrationRequestForAdmin;
import org.itmo.eventapp.main.model.entity.enums.EmailStatus;
import org.itmo.eventapp.main.model.entity.enums.RegistrationRequestStatus;
import org.itmo.eventapp.main.repository.*;
import org.itmo.eventapp.main.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserLoginInfoRepository userLoginInfoRepository;
    private final UserNotificationInfoRepository userNotificationInfoRepository;
    private final RoleRepository roleRepository;
    private final RegistrationRequestRepository registrationRequestRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    private final String defaultRole = "Читатель";

    @Autowired
    private MailSenderService mailSenderService;
    private RoleService roleService;

    public String login(LoginRequest loginRequest) throws BadCredentialsException {

        try {
            var authentication =
                    new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password());
            authenticationManager.authenticate(authentication);

            return jwtTokenUtil.generateToken(loginRequest.login());
        }
        catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка аутентификации.");
        }
    }

    public void createRegisterRequest(RegistrationUserRequest registrationUserRequest) {
        String login = registrationUserRequest.email();

        Optional<RegistrationRequest> info = registrationRequestRepository.getRegistrationRequestByEmail(login);
        if (info.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("Запрос с email '%s' уже существует.", login));
        }

        RegistrationRequest registrationRequest = new RegistrationRequest();

        registrationRequest.setEmail(registrationUserRequest.email());
        registrationRequest.setPasswordHash(passwordEncoder.encode(registrationUserRequest.password()));
        registrationRequest.setName(registrationUserRequest.name());
        registrationRequest.setSurname(registrationUserRequest.surname());
        registrationRequest.setStatus(RegistrationRequestStatus.NEW);
        registrationRequest.setSentTime(LocalDateTime.now());

        registrationRequestRepository.save(registrationRequest);
    }

    @Transactional
    public void approveRegistrationRequestCallback(int requestId) {

        var request = registrationRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрос на регистрацию не найден"));

        if (request.getStatus() != RegistrationRequestStatus.NEW) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Запрос уже обработан");
        }

        UserLoginInfo loginInfo = new UserLoginInfo();
        User user = new User();
        UserNotificationInfo notificationInfo = new UserNotificationInfo();

        var reader = roleService.getReaderRole();

        notificationInfo.setDevices(new String[] {});
        notificationInfo.setEnableEmailNotifications(false);
        notificationInfo.setEnablePushNotifications(false);

        userNotificationInfoRepository.save(notificationInfo);

        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setRole(reader);
        user.setUserNotificationInfo(notificationInfo);

        userRepository.save(user);

        loginInfo.setRegistration(request);
        loginInfo.setEmail(request.getEmail());
        loginInfo.setPasswordHash(request.getPasswordHash());
        loginInfo.setLastLoginDate(LocalDateTime.now());
        loginInfo.setUser(user);
        loginInfo.setEmailStatus(EmailStatus.APPROVED);

        userLoginInfoRepository.save(loginInfo);

        request.setStatus(RegistrationRequestStatus.APPROVED);
        registrationRequestRepository.save(request);

        try {
            mailSenderService.sendApproveRegistrationRequestMessage(request.getEmail(), request.getName());
        } catch (MessagingException | IOException e) {}
    }

    @Transactional
    public void declineRegistrationRequestCallback(int requestId) {
        var request = registrationRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрос на регистрацию не найден"));

        if (request.getStatus() != RegistrationRequestStatus.NEW) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Запрос уже обработан");
        }

        request.setStatus(RegistrationRequestStatus.DECLINED);
        registrationRequestRepository.save(request);

        try {
            mailSenderService.sendDeclineRegistrationRequestMessage(request.getEmail(), request.getName());
        } catch (MessagingException | IOException e) {}
    }

    public List<RegistrationRequestForAdmin> listRegisterRequestsCallback() {
        return registrationRequestRepository.getRegistrationRequestsByStatus(RegistrationRequestStatus.NEW)
                .stream()
                .map((request) -> new RegistrationRequestForAdmin(
                        request.getEmail(),
                        request.getName(),
                        request.getSurname(),
                        request.getStatus(),
                        request.getSentTime()))
                .toList();
    }
}
