package org.itmo.eventapp.main.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.dto.request.RoleRequest;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.enums.RoleType;
import org.itmo.eventapp.main.model.mapper.PrivilegeMapper;
import org.itmo.eventapp.main.model.mapper.RoleMapper;
import org.itmo.eventapp.main.repository.EventRoleRepository;
import org.itmo.eventapp.main.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegeService privilegeService;
    private final UserService userService;
    private final List<String> basicRoles = Arrays.asList("Администратор", "Читатель", "Организатор", "Помощник");
    private final EventRoleRepository eventRoleRepository;
    private final UserLoginInfoService userLoginInfoService;

    @Transactional
    public Role createRole(RoleRequest roleRequest) {
        if (roleRepository.findByName(roleRequest.name()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionConst.ROLE_EXIST_MESSAGE);
        var privileges = roleRequest.privileges().stream().map(privilegeService::findById);
        Role role = RoleMapper.roleRequestToRole(roleRequest, privileges);
        return roleRepository.save(role);
    }

    @Transactional
    public Role editRole(Integer id, RoleRequest roleRequest) {
        var editedRole = findRoleById(id);
        if (basicRoles.contains(editedRole.getName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.ROLE_EDITING_FORBIDDEN_MESSAGE);
        var role = roleRepository.findByName(roleRequest.name());
        if (role.isPresent() && !role.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionConst.ROLE_EXIST_MESSAGE);
        }
        editedRole.setName(roleRequest.name());
        editedRole.setDescription(roleRequest.description());
        editedRole.setType(roleRequest.isEvent() ? RoleType.EVENT : RoleType.SYSTEM);
        editedRole.setPrivileges(new HashSet<>());
        var privileges = roleRequest.privileges().stream().map(privilegeService::findById);
        editedRole.setPrivileges(PrivilegeMapper.privilegeStreamToPrivilegeSet(privileges, roleRequest.isEvent()));
        return roleRepository.save(editedRole);
    }

    @Transactional
    public void deleteRole(Integer id) {
        var role = findRoleById(id);
        if (basicRoles.contains(role.getName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.ROLE_DELETING_FORBIDDEN_MESSAGE);
        if (role.getType().equals(RoleType.SYSTEM)) {
            if (userService.existsByRoleId(id))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.USERS_WITH_ROLE_EXIST);
        } else {
            // TODO: временно, надо будет переделать
            if (eventRoleRepository.existsByRoleId(id))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.USERS_WITH_ROLE_EXIST);
        }
        role.setPrivileges(null);
        roleRepository.deleteById(id);
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role findRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ExceptionConst.ROLE_ID_NOT_FOUND_MESSAGE.formatted(id)));
    }

    public Role findOrganizationalRoleById(Integer id) {
        var role = findRoleById(id);
        if (role.getType().equals(RoleType.SYSTEM))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    ExceptionConst.INVALID_ROLE_TYPE.formatted("организационная"));
        return role;
    }

    public List<Role> getOrganizational() {
        return roleRepository.findAllByType(RoleType.EVENT);
    }

    public List<Role> searchByName(String name) {
        return roleRepository.findByNameContainingIgnoreCase(name);
    }

    public void assignSystemRole(String login, Integer userId, Integer roleId) {
        var user = userService.findById(userId);
        var userWithEmail = userLoginInfoService.findByLogin(login);
        if (userWithEmail.getUser().getId().equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.ASSIGN_SELF_ROLE_FORBIDDEN_MESSAGE);
        var role = findRoleById(roleId);
        if (role.getType().equals(RoleType.EVENT))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.INVALID_ROLE_TYPE.formatted("системная"));
        user.setRole(role);
        userService.save(user);
    }

    public void revokeSystemRole(String login, Integer userId) {
        var user = userService.findById(userId);
        var userWithEmail = userLoginInfoService.findByLogin(login);
        if (userWithEmail.getUser().getId().equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.REVOKE_SELF_ROLE_FORBIDDEN_MESSAGE);
        user.setRole(getReaderRole());
        userService.save(user);
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ExceptionConst.ROLE_NAME_NOT_FOUND_MESSAGE.formatted(name)));
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
