package org.itmo.eventapp.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.RoleRequest;
import org.itmo.eventapp.main.model.dto.response.PrivilegeResponse;
import org.itmo.eventapp.main.model.dto.response.RoleResponse;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeType;
import org.itmo.eventapp.main.model.mapper.PrivilegeMapper;
import org.itmo.eventapp.main.model.mapper.RoleMapper;
import org.itmo.eventapp.main.service.EventRoleService;
import org.itmo.eventapp.main.service.PrivilegeService;
import org.itmo.eventapp.main.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
@Validated
public class RoleController {

    private final RoleService roleService;
    private final PrivilegeService privilegeService;
    private final EventRoleService eventRoleService;

    @Operation(summary = "Получение роли по id")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@Positive(message = "Параметр roleId не может быть меньше 1!")
                                                    @PathVariable @Parameter(name = "id", description = "ID роли", example = "1") Integer id) {
        return ResponseEntity.ok().body(RoleMapper.roleToRoleResponse(roleService.findRoleById(id)));
    }

    @Operation(summary = "Получение списка всех ролей")
    @GetMapping("/")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(RoleMapper.rolesToRoleResponseList(roleService.getAll()));
    }

    @Operation(summary = "Получение организационных ролей")
    @GetMapping("/organizational")
    public ResponseEntity<List<RoleResponse>> getOrganizationalRoles() {
        return ResponseEntity.ok(RoleMapper.rolesToRoleResponseList(roleService.getOrganizational()));
    }

    @Operation(summary = "Поиск ролей по совпадению в названии")
    @GetMapping("/search")
    public ResponseEntity<List<RoleResponse>> searchByName(@RequestParam @Parameter(name = "name", description = "Имя роли", example = "1") String name) {
        return ResponseEntity.ok(RoleMapper.rolesToRoleResponseList(roleService.searchByName(name)));
    }

    @Operation(summary = "Создание роли")
    @PostMapping("/")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(RoleMapper.roleToRoleResponse(roleService.createRole(roleRequest)));
    }

    @Operation(summary = "Удаление роли")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@Positive(message = "Параметр roleId не может быть меньше 1!")
                                           @PathVariable @Parameter(name = "id", description = "ID роли", example = "1") Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Редактирование роли")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> editRole(@Positive(message = "Параметр roleId не может быть меньше 1!")
                                                 @PathVariable @Parameter(name = "id", description = "ID роли", example = "1") Integer id,
                                                 @Valid @RequestBody RoleRequest roleRequest) {
        return ResponseEntity.ok(RoleMapper.roleToRoleResponse(roleService.editRole(id, roleRequest)));
    }

    @Operation(summary = "Получение списка всех привилегий")
    @GetMapping("/privileges")
    public ResponseEntity<List<PrivilegeResponse>> getAllPrivileges() {
        return ResponseEntity.ok(PrivilegeMapper.privilegesToPrivilegeResponseList(privilegeService.getAll()));
    }

    @Operation(summary = "Получение списка системных привилегий")
    @GetMapping("/system-privileges")
    public ResponseEntity<List<PrivilegeResponse>> getSystemPrivileges() {
        return ResponseEntity.ok(PrivilegeMapper.privilegesToPrivilegeResponseList(privilegeService.getPrivilegeByType(PrivilegeType.SYSTEM)));
    }

    @Operation(summary = "Получение списка организационных привилегий")
    @GetMapping("/organizational-privileges")
    public ResponseEntity<List<PrivilegeResponse>> getOrganizationalPrivileges() {
        return ResponseEntity.ok(PrivilegeMapper.privilegesToPrivilegeResponseList(privilegeService.getPrivilegeByType(PrivilegeType.EVENT)));
    }

    @Operation(summary = "Назначение пользователю организационной роли")
    @PostMapping("/organizational/{userId}/{eventId}/{roleId}")
    public ResponseEntity<Void> assignOrganizationalRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @Parameter(name = "userId", description = "ID пользователя", example = "1") @PathVariable Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @Parameter(name = "eventId", description = "ID мероприятия", example = "1") @PathVariable Integer eventId,
            @Positive(message = "Параметр roleId не может быть меньше 1!") @Parameter(name = "roleId", description = "ID роли", example = "1") @PathVariable Integer roleId) {
        eventRoleService.assignOrganizationalRole(userId, roleId, eventId, Boolean.FALSE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Назначение пользователю роли Организатор")
    @PostMapping("/organizer/{userId}/{eventId}")
    public ResponseEntity<Void> assignOrganizerRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable @Parameter(name = "userId", description = "ID пользователя", example = "1") Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @PathVariable @Parameter(name = "eventId", description = "ID мероприятия", example = "1") Integer eventId) {
        eventRoleService.assignOrganizationalRole(userId, roleService.getOrganizerRole().getId(), eventId, Boolean.TRUE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Назначение пользователю роли Помощник")
    @PostMapping("assistant/{userId}/{eventId}")
    public ResponseEntity<Void> assignAssistantRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable @Parameter(name = "userId", description = "ID пользователя", example = "1") Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @PathVariable @Parameter(name = "eventId", description = "ID мероприятия", example = "1") Integer eventId) {
        eventRoleService.assignOrganizationalRole(userId, roleService.getAssistantRole().getId(), eventId, Boolean.TRUE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Лишение пользователя организационной роли")
    @DeleteMapping("/organizational/{userId}/{eventId}/{roleId}")
    public ResponseEntity<Void> revokeOrganizationalRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable @Parameter(name = "userId", description = "ID пользователя", example = "1") Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @PathVariable @Parameter(name = "eventId", description = "ID мероприятия", example = "1") Integer eventId,
            @Positive (message = "Параметр roleId не может быть меньше 1!") @PathVariable @Parameter(name = "roleId", description = "ID роли", example = "1") Integer roleId) {
        eventRoleService.revokeOrganizationalRole(userId, roleId, eventId, Boolean.FALSE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Лишение пользователя роли Организатор")
    @DeleteMapping("/organizer/{userId}/{eventId}")
    public ResponseEntity<Void> revokeOrganizerRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @PathVariable Integer eventId) {
        eventRoleService.revokeOrganizationalRole(userId, roleService.getOrganizerRole().getId(), eventId, Boolean.TRUE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Лишение пользователя роли Помощник")
    @DeleteMapping("/assistant/{userId}/{eventId}")
    public ResponseEntity<Void> revokeAssistantRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @PathVariable Integer eventId) {
        eventRoleService.revokeOrganizationalRole(userId, roleService.getAssistantRole().getId(), eventId, Boolean.TRUE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Назначение пользователю системной роли")
    @PutMapping("/system/{userId}/{roleId}")
    public ResponseEntity<Void> assignSystemRole(
            Authentication authentication,
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable @Parameter(name = "userId", description = "ID пользователя", example = "1") Integer userId,
            @Positive(message = "Параметр roleId не может быть меньше 1!") @PathVariable @Parameter(name = "roleId", description = "ID роли", example = "1") Integer roleId) {
        roleService.assignSystemRole(authentication.getName(), userId, roleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Лишение пользователя системной роли")
    @PutMapping("/system-revoke/{userId}")
    public ResponseEntity<Void> revokeSystemRole(
            Authentication authentication,
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable @Parameter(name = "userId", description = "ID пользователя", example = "1") Integer userId) {
        roleService.revokeSystemRole(authentication.getName(), userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
