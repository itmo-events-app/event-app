package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.response.PrivilegeResponse;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeType;
import org.itmo.eventapp.main.repository.PrivilegeRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;

    public Privilege findById(Integer id) {
        return privilegeRepository.findPrivilegeById(id);
    }

    public List<PrivilegeResponse> getOrganizational() {
        return privilegeRepository.findAllByType(PrivilegeType.EVENT);
    }

    public List<PrivilegeResponse> getSystem() {
        return privilegeRepository.findAllByType(PrivilegeType.SYSTEM);
    }

    public List<PrivilegeResponse> getAll() {
        return convertToDto(privilegeRepository.findAll());
    }

    public List<PrivilegeResponse> convertToDto(Collection<Privilege> privileges) {
        return privileges.stream()
                .map(privilege -> new PrivilegeResponse(privilege.getId(),
                        privilege.getName(),
                        privilege.getDescription()))
                .toList();
    }
}
