package org.itmo.eventapp.main.service;


import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.model.dto.LoginRequest;
import org.itmo.eventapp.main.model.dto.RegistrationUserRequest;
import org.itmo.eventapp.main.repository.RegistrationRequestRepository;
import org.itmo.eventapp.main.repository.RoleRepository;
import org.itmo.eventapp.main.repository.UserLoginInfoRepository;
import org.itmo.eventapp.main.repository.UserRepository;
import org.itmo.eventapp.main.security.util.JwtTokenUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserLoginInfoRepository userLoginInfoRepository;
    private final RoleRepository roleRepository;
    private final RegistrationRequestRepository registrationRequestRepository;

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public String login(LoginRequest loginRequest) {

        var authentication =
                new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password());
        authenticationManager.authenticate(authentication);

        return jwtTokenUtil.generateToken(loginRequest.login());
    }

    public void createRegisterRequest(RegistrationUserRequest registrationUserRequest) {
        String login = registrationUserRequest.email();

        Optional<RegistrationRequest> info = registrationRequestRepository.getRegistrationRequestByEmail(login);
        if (info.isPresent()) {
            throw new DuplicateKeyException(String.format("User with the email address '%s' already exists.", login));
        }

        RegistrationRequest registrationRequest = new RegistrationRequest();

        registrationRequest.setEmail(registrationUserRequest.email());
        registrationRequest.setPasswordHash(registrationUserRequest.password());
        registrationRequest.setName(registrationUserRequest.name());
        registrationRequest.setSurname(registrationUserRequest.surname());
        registrationRequest.setSentTime(LocalDateTime.now());

        registrationRequestRepository.save(registrationRequest);
    }

    public void approveRegistrationRequestCallback(int requestId) {

        var request = registrationRequestRepository.findById(requestId);

        if (request.isEmpty()) {
            // TODO Exception
            return;
        }

        UserLoginInfo loginInfo = new UserLoginInfo();
        User user = new User();

        var reader = roleRepository.findByName("Читатель");

        if (reader.isEmpty()) {
            // TODO Exception
            return;
        }

        loginInfo.setRegistration(request.get());
        loginInfo.setEmail(request.get().getEmail());
        loginInfo.setPasswordHash(request.get().getPasswordHash());
        loginInfo.setLastLoginDate(LocalDateTime.now());

        userLoginInfoRepository.save(loginInfo);

        user.setName(request.get().getName());
        user.setSurname(request.get().getSurname());
        user.setRole(reader.get());
        user.setUserNotificationInfo(new UserNotificationInfo());
        user.setUserLoginInfo(loginInfo);

        userRepository.save(user);
    }
}
