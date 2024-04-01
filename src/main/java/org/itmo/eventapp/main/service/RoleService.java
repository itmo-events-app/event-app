package org.itmo.eventapp.main.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exception.NotFoundException;
import org.itmo.eventapp.main.model.dto.request.RoleRequest;
import org.itmo.eventapp.main.model.dto.response.RoleResponse;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.enums.RoleType;
import org.itmo.eventapp.main.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = new Role(roleRequest);
        Role createdRole = roleRepository.save(role);
        return new RoleResponse(createdRole.getId(), createdRole.getName(), createdRole.getDescription());
    }

    @Transactional
    public void deleteRole(Integer id) {
        var role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Role with id %d doesn't exist", id)));
        // TODO: Add checks
        role.setPrivileges(null);
        roleRepository.deleteById(id);
    }

    public List<RoleResponse> getAll() {
        var roles = roleRepository.findAll();
        return roles.stream().map(role -> RoleResponse.builder()
                .name(role.getName())
                .description(role.getDescription())
                .id(role.getId())
                .build()).toList();
    }

    public RoleResponse findById(Integer id) {
        var role = roleRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Role with id %d doesn't exist", id)));
        return new RoleResponse(id, role.getName(), role.getDescription());
    }

    public List<RoleResponse> getOrganizational() {
        return roleRepository.findAllByType(RoleType.EVENT);
    }

    public List<RoleResponse> searchByName(String name) {
        return roleRepository.findByNameContainingIgnoreCase(name);
    }

    private Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role getReaderRole() {
        return findByName("Читатель");
    }

    public Role getAdminRole() {
        return findByName("Администратор");
    }

    public Role getAssistantRole() {
        return findByName("Помощник");
    }

    public Role getOrganizerRole() {
        return findByName("Организатор");
    }
}
