package org.itmo.eventapp.main.service;


import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.model.dto.LoginRequest;
import org.itmo.eventapp.main.model.dto.RegistrationUserRequest;
import org.itmo.eventapp.main.model.entity.enums.EmailStatus;
import org.itmo.eventapp.main.model.entity.enums.RegistrationRequestStatus;
import org.itmo.eventapp.main.repository.*;
import org.itmo.eventapp.main.security.util.JwtTokenUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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

        UserLoginInfo loginInfo = new UserLoginInfo();
        User user = new User();
        UserNotificationInfo notificationInfo = new UserNotificationInfo();

        var reader = roleRepository.findByName("Читатель")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Роли 'Читатель' не существует"));

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
    }
}
