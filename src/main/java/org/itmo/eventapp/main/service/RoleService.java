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
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }

        return role.get();
    }

    Role findByName(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }

        return role.get();
    }

}
