package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.response.PrivilegeResponse;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeType;
import org.itmo.eventapp.main.model.mapper.PrivilegeMapper;
import org.itmo.eventapp.main.repository.PrivilegeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;

    public Privilege findById(Integer id) {
        return privilegeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Привилегия с id %d не существует", id)));
    }

    public List<Privilege> getPrivilegeByType(PrivilegeType type) {
        return privilegeRepository.findAllByType(type);
    }

    public List<Privilege> getAll() {
        return privilegeRepository.findAll();
    }
}
