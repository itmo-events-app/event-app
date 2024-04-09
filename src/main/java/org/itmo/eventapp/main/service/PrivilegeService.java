package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeType;
import org.itmo.eventapp.main.repository.PrivilegeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;

    public Privilege findById(Integer id) {
        return privilegeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                ExceptionConst.PRIVILEGE_ID_NOT_FOUND_MESSAGE.formatted(id)));
    }

    public List<Privilege> getPrivilegeByType(PrivilegeType type) {
        List<Privilege> privileges = privilegeRepository.findAllByType(type);
        if (type.equals(PrivilegeType.SYSTEM)) {
            var assignOrganizerPrivilege = privilegeRepository.findByName(PrivilegeName.ASSIGN_ORGANIZER_ROLE);
            assignOrganizerPrivilege.ifPresent(privileges::add);
        }
        return privileges;
    }

    public List<Privilege> getAll() {
        return privilegeRepository.findAll();
    }
}
