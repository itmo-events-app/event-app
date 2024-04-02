package org.itmo.eventapp.main.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exception.IncorrectRoleTypeException;
import org.itmo.eventapp.main.exception.NotAllowedException;
import org.itmo.eventapp.main.exception.NotFoundException;
import org.itmo.eventapp.main.exception.NotUniqueException;
import org.itmo.eventapp.main.model.dto.request.RoleRequest;
import org.itmo.eventapp.main.model.dto.response.RoleResponse;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.enums.RoleType;
import org.itmo.eventapp.main.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegeService privilegeService;
    private final UserService userService;
//    private final EventRoleService eventRoleService;
    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        if (roleRepository.findByName(roleRequest.name()).isPresent())
            throw new NotUniqueException("Роль с таким именем уже существует");
        Role role = new Role(roleRequest);
        roleRequest.privileges().stream()
                .map(privilegeService::findById)
                .forEach(role::addPrivilege);
        Role createdRole = roleRepository.save(role);
        return new RoleResponse(createdRole.getId(),
                createdRole.getName(),
                createdRole.getDescription(),
                createdRole.getType(),
                privilegeService.convertToDto(createdRole.getPrivileges()));
    }

    @Transactional
    public RoleResponse editRole(Integer id, RoleRequest roleRequest) {
        var role = roleRepository.findByName(roleRequest.name());
        if (role.isPresent() && !role.get().getId().equals(id))
            throw new NotUniqueException("Роль с таким именем уже существует");

        var editedRole = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Роль с id %d не существует", id)));

        editedRole.setName(roleRequest.name());
        editedRole.setDescription(roleRequest.description());
        editedRole.setType(roleRequest.isEvent() ? RoleType.EVENT : RoleType.SYSTEM);
        editedRole.setPrivileges(new HashSet<>());
        roleRequest.privileges().stream()
                .map(privilegeService::findById)
                .forEach(editedRole::addPrivilege);
        roleRepository.save(editedRole);
        return new RoleResponse(editedRole.getId(),
                editedRole.getName(),
                editedRole.getDescription(),
                editedRole.getType(),
                privilegeService.convertToDto(editedRole.getPrivileges()));
    }

    @Transactional
    public void deleteRole(Integer id) {
        var role = findRoleById(id);
        if (role.getType().equals(RoleType.SYSTEM)) {
            if (!userService.findAllByRole(role).isEmpty())
                throw new NotAllowedException("Невозможно удалить роль, так как существуют пользователи, которым она назначена");
        } // else {
//            if (!eventRoleService.findAllByRole(role).isEmpty())
//                throw new NotAllowedException("Невозможно удалить роль, так как существуют пользователи, которым она назначена");
//        }
        role.setPrivileges(null);
        roleRepository.deleteById(id);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream()
                .map(role -> new RoleResponse(role.getId(),
                        role.getName(),
                        role.getDescription(),
                        role.getType(),
                        privilegeService.convertToDto(role.getPrivileges())))
                .toList();
    }

    public RoleResponse findById(Integer id) {
        var role = findRoleById(id);
        return new RoleResponse(id,
                role.getName(),
                role.getDescription(),
                role.getType(),
                privilegeService.convertToDto(role.getPrivileges()));
    }

    public Role findRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Роль с id %d не существует", id)));
    }

    public List<RoleResponse> getOrganizational() {
        return roleRepository.findAllByType(RoleType.EVENT).stream()
                .map(role -> new RoleResponse(role.getId(),
                        role.getName(),
                        role.getDescription(),
                        role.getType(),
                        privilegeService.convertToDto(role.getPrivileges())))
                .toList();
    }

    public List<RoleResponse> searchByName(String name) {
        return roleRepository.findByNameContainingIgnoreCase(name).stream()
                .map(role -> new RoleResponse(role.getId(),
                        role.getName(),
                        role.getDescription(),
                        role.getType(),
                        privilegeService.convertToDto(role.getPrivileges())))
                .toList();
    }

    public void assignSystemRole(Integer userId, Integer roleId) {
        var user = userService.findById(userId);
        var role = findRoleById(roleId);
        if (role.getType().equals(RoleType.EVENT)) throw new IncorrectRoleTypeException("Неверный тип роли");
        user.setRole(role);
        userService.save(user);
    }

    public void revokeSystemRole(Integer userId) {
        var user = userService.findById(userId);
        user.setRole(getReaderRole());
        userService.save(user);
    }

    private Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(String.format("Роль с именем %s не существует", name)));
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
