package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exception.NotFoundException;
import org.itmo.eventapp.main.model.dto.RoleDto;
import org.itmo.eventapp.main.model.entity.RoleType;
import org.itmo.eventapp.main.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public void deleteRole(Integer id) throws NotFoundException {
        if (!roleRepository.existsById(id))
            throw new NotFoundException(String.format("Role with id %d doesn't exist", id));
        // TODO: Add checks
        roleRepository.deleteById(id);
    }

    public List<RoleDto> getAll() {
        var roles = roleRepository.findAll();
        return roles.stream().map(role -> RoleDto.builder()
                .name(role.getName())
                .description(role.getDescription())
                .id(role.getId())
                .build()).toList();
    }

    public List<RoleDto> getOrganizational() {
        return roleRepository.findAllByType(RoleType.EVENT);
    }
}
