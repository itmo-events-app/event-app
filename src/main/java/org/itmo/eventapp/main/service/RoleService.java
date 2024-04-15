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
        var roleType = Boolean.TRUE.equals(roleRequest.isEvent()) ? RoleType.EVENT : RoleType.SYSTEM;
        if (!editedRole.getType().equals(roleType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.ROLE_TYPE_CHANGING_FORBIDDEN_MESSAGE);
        }
        editedRole.setName(roleRequest.name());
        editedRole.setDescription(roleRequest.description());
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
            if (userService.existsByRolesId(id))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.USERS_WITH_ROLE_EXIST);
        } else {
            // TODO: временно, надо будет переделать
            if (eventRoleRepository.existsByRoleId(id))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.USERS_WITH_ROLE_EXIST);
        }
        role.setPrivileges(null);
        roleRepository.deleteById(id);
    }

    public List<Role> getRoles(RoleType type) {
        if (type == null) {
            return roleRepository.findAll();
        } else {
            return roleRepository.findAllByType(type);
        }
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

    public List<Role> searchByName(String name) {
        return roleRepository.findByNameContainingIgnoreCase(name);
    }

    public void assignSystemRole(Integer currentPrincipalId, Integer userId, Integer roleId) {
        if (currentPrincipalId.equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.ASSIGN_SELF_ROLE_FORBIDDEN_MESSAGE);
        var user = userService.findById(userId);
        var role = findRoleById(roleId);
        if (role.getType().equals(RoleType.EVENT))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.INVALID_ROLE_TYPE.formatted("системная"));
        if (!user.getRoles().contains(role)) {
            user.addRole(role);
            userService.save(user);
        }
    }

    public void revokeSystemRole(Integer currentPrincipalId, Integer userId, Integer roleId) {
        if (currentPrincipalId.equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.REVOKE_SELF_ROLE_FORBIDDEN_MESSAGE);
        var user = userService.findById(userId);
        var role = findRoleById(roleId);
        if (role.getType().equals(RoleType.EVENT))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionConst.INVALID_ROLE_TYPE.formatted("системная"));
        var roles = user.getRoles();
        if (roles.size() == 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionConst.AT_LEAST_ONE_SYSTEM_ROLE_MESSAGE);
        }
        if (roles.contains(role)) {
            user.removeRole(role);
            userService.save(user);
        }
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
