package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
}
