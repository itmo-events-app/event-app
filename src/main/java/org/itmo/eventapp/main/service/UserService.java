package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exception.NotFoundException;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не существует", id)));
    }

    public List<User> findAllByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
