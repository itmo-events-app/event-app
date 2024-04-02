package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    Role findById(int id) {
        return roleRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Role event not found"));
    }

    Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Role event not found"));
    }

}
