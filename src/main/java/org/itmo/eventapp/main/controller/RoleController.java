package org.itmo.eventapp.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.RoleRequest;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.model.dto.response.PrivilegeResponse;
import org.itmo.eventapp.main.model.dto.response.RoleResponse;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeType;
import org.itmo.eventapp.main.model.entity.enums.RoleType;
import org.itmo.eventapp.main.model.mapper.EventMapper;
import org.itmo.eventapp.main.model.mapper.PrivilegeMapper;
import org.itmo.eventapp.main.model.mapper.RoleMapper;
import org.itmo.eventapp.main.service.EventRoleService;
import org.itmo.eventapp.main.service.PrivilegeService;
import org.itmo.eventapp.main.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("@roleSecurityExpression.canCreateRole() or @roleSecurityExpression.canEditRole() or @roleSecurityExpression.canDeleteRole()")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(
            @Positive(message = "Параметр id не может быть меньше 1!")
            @PathVariable @Parameter(name = "id", description = "ID роли", example = "1") Integer id) {
        return ResponseEntity.ok().body(RoleMapper.roleToRoleResponse(roleService.findRoleById(id)));
    }

    @Operation(summary = "Получение организационной роли по id")
    // TODO: временно, возможно, нужно переделать
    @PreAuthorize("@roleSecurityExpression.canGetAllOrganizationalRole(#eventId)")
    @GetMapping("/organizational/{roleId}")
    public ResponseEntity<RoleResponse> getOrganizationalRoleById(
            @Positive(message = "Параметр roleId не может быть меньше 1!")
            @PathVariable @Parameter(name = "roleId", description = "ID роли", example = "1") Integer roleId,
            @Positive(message = "Параметр eventId не может быть меньше 1!")
            @RequestParam @Parameter(name = "eventId", description = "ID меропрятия", example = "1") Integer eventId) {
        return ResponseEntity.ok().body(RoleMapper.roleToRoleResponse(roleService.findOrganizationalRoleById(roleId)));
    }

    @Operation(summary = "Получение списка всех ролей")
    @PreAuthorize("@roleSecurityExpression.canCreateRole() or @roleSecurityExpression.canEditRole() or @roleSecurityExpression.canDeleteRole()")
    @GetMapping("/")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(RoleMapper.rolesToRoleResponseList(roleService.getRoles(null)));
    }

    @Operation(summary = "Получение списка системных ролей")
    @PreAuthorize("@roleSecurityExpression.canAssignSystemRole()")
    @GetMapping("/system")
    public ResponseEntity<List<RoleResponse>> getSystemRoles() {
        return ResponseEntity.ok(RoleMapper.rolesToRoleResponseList(roleService.getRoles(RoleType.SYSTEM)));
    }

    @Operation(summary = "Получение списка организационных ролей")
    // TODO: временно, возможно, нужно переделать
    @PreAuthorize("@roleSecurityExpression.canGetAllOrganizationalRole(#eventId)")
    @GetMapping("/organizational")
    public ResponseEntity<List<RoleResponse>> getOrganizationalRoles(
            @Positive(message = "Параметр eventId не может быть меньше 1!")
            @RequestParam @Parameter(name = "eventId", description = "ID меропрятия", example = "1") Integer eventId) {
        return ResponseEntity.ok(RoleMapper.rolesToRoleResponseList(roleService.getRoles(RoleType.EVENT)));
    }

    @Operation(summary = "Поиск ролей по совпадению в названии")
    @PreAuthorize("@roleSecurityExpression.canCreateRole() or @roleSecurityExpression.canEditRole() or @roleSecurityExpression.canDeleteRole()")
    @GetMapping("/search")
    public ResponseEntity<List<RoleResponse>> searchByName(@RequestParam @Parameter(name = "name", description = "Имя роли", example = "1") String name) {
        return ResponseEntity.ok(RoleMapper.rolesToRoleResponseList(roleService.searchByName(name)));
    }

    @Operation(summary = "Создание роли")
    @PreAuthorize("@roleSecurityExpression.canCreateRole()")
    @PostMapping("/")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(RoleMapper.roleToRoleResponse(roleService.createRole(roleRequest)));
    }

    @Operation(summary = "Удаление роли")
    @PreAuthorize("@roleSecurityExpression.canDeleteRole()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@Positive(message = "Параметр roleId не может быть меньше 1!")
                                           @PathVariable @Parameter(name = "id", description = "ID роли", example = "1") Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Редактирование роли")
    @PreAuthorize("@roleSecurityExpression.canEditRole()")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> editRole(@Positive(message = "Параметр roleId не может быть меньше 1!")
                                                 @PathVariable @Parameter(name = "id", description = "ID роли", example = "1") Integer id,
                                                 @Valid @RequestBody RoleRequest roleRequest) {
        return ResponseEntity.ok(RoleMapper.roleToRoleResponse(roleService.editRole(id, roleRequest)));
    }

    @Operation(summary = "Получение списка привилегий")
    @PreAuthorize("@roleSecurityExpression.canCreateRole() or @roleSecurityExpression.canEditRole()")
    @GetMapping("/privileges")
    public ResponseEntity<List<PrivilegeResponse>> getAllPrivileges(
            @Parameter(name = "type", description = "Тип привилегии", example = "SYSTEM") @RequestParam(required = false) PrivilegeType type) {
        return ResponseEntity.ok(PrivilegeMapper.privilegesToPrivilegeResponseList(privilegeService.getPrivileges(type)));
    }

    @Operation(summary = "Назначение пользователю организационной роли")
    @PreAuthorize("@roleSecurityExpression.canAssignOrganizationalRole(#eventId)")
    @PostMapping("/organizational/{userId}/{eventId}/{roleId}")
    public ResponseEntity<Void> assignOrganizationalRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @Parameter(name = "userId", description = "ID пользователя", example = "1") @PathVariable Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @Parameter(name = "eventId", description = "ID мероприятия", example = "1") @PathVariable Integer eventId,
            @Positive(message = "Параметр roleId не может быть меньше 1!") @Parameter(name = "roleId", description = "ID роли", example = "1") @PathVariable Integer roleId) {
        eventRoleService.assignOrganizationalRole(userId, roleId, eventId, Boolean.FALSE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Назначение пользователю роли Организатор")
    @PreAuthorize("@roleSecurityExpression.canAssignOrganizerRole(#eventId)")
    @PostMapping("/organizer/{userId}/{eventId}")
    public ResponseEntity<Void> assignOrganizerRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable @Parameter(name = "userId", description = "ID пользователя", example = "1") Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @PathVariable @Parameter(name = "eventId", description = "ID мероприятия", example = "1") Integer eventId) {
        eventRoleService.assignOrganizationalRole(userId, roleService.getOrganizerRole().getId(), eventId, Boolean.TRUE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Назначение пользователю роли Помощник")
    @PreAuthorize("@roleSecurityExpression.canAssignAssistantRole(#eventId)")
    @PostMapping("assistant/{userId}/{eventId}")
    public ResponseEntity<Void> assignAssistantRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable @Parameter(name = "userId", description = "ID пользователя", example = "1") Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @PathVariable @Parameter(name = "eventId", description = "ID мероприятия", example = "1") Integer eventId) {
        eventRoleService.assignOrganizationalRole(userId, roleService.getAssistantRole().getId(), eventId, Boolean.TRUE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Лишение пользователя организационной роли")
    @PreAuthorize("@roleSecurityExpression.canRevokeOrganizationalRole(#eventId)")
    @DeleteMapping("/organizational/{userId}/{eventId}/{roleId}")
    public ResponseEntity<Void> revokeOrganizationalRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable @Parameter(name = "userId", description = "ID пользователя", example = "1") Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @PathVariable @Parameter(name = "eventId", description = "ID мероприятия", example = "1") Integer eventId,
            @Positive(message = "Параметр roleId не может быть меньше 1!") @PathVariable @Parameter(name = "roleId", description = "ID роли", example = "1") Integer roleId) {
        eventRoleService.revokeOrganizationalRole(userId, roleId, eventId, Boolean.FALSE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Лишение пользователя роли Организатор")
    @PreAuthorize("@roleSecurityExpression.canRevokeOrganizerRole()")
    @DeleteMapping("/organizer/{userId}/{eventId}")
    public ResponseEntity<Void> revokeOrganizerRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @PathVariable Integer eventId) {
        eventRoleService.revokeOrganizationalRole(userId, roleService.getOrganizerRole().getId(), eventId, Boolean.TRUE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Лишение пользователя роли Помощник")
    @PreAuthorize("@roleSecurityExpression.canRevokeAssistantRole(#eventId)")
    @DeleteMapping("/assistant/{userId}/{eventId}")
    public ResponseEntity<Void> revokeAssistantRole(
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable Integer userId,
            @Positive(message = "Параметр eventId не может быть меньше 1!") @PathVariable Integer eventId) {
        eventRoleService.revokeOrganizationalRole(userId, roleService.getAssistantRole().getId(), eventId, Boolean.TRUE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Назначение пользователю системной роли")
    @PreAuthorize("@roleSecurityExpression.canAssignSystemRole()")
    @PutMapping("/system/{userId}/{roleId}")
    public ResponseEntity<Void> assignSystemRole(
            @AuthenticationPrincipal UserLoginInfo userDetails,
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable @Parameter(name = "userId", description = "ID пользователя", example = "1") Integer userId,
            @Positive(message = "Параметр roleId не может быть меньше 1!") @PathVariable @Parameter(name = "roleId", description = "ID роли", example = "1") Integer roleId) {
        roleService.assignSystemRole(userDetails.getUser().getId(), userId, roleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Лишение пользователя системной роли")
    @PreAuthorize("@roleSecurityExpression.canRevokeSystemRole()")
    @PutMapping("/system-revoke/{userId}")
    public ResponseEntity<Void> revokeSystemRole(
            @AuthenticationPrincipal UserLoginInfo userDetails,
            @Positive(message = "Параметр userId не может быть меньше 1!") @PathVariable @Parameter(name = "userId", description = "ID пользователя", example = "1") Integer userId) {
        roleService.revokeSystemRole(userDetails.getUser().getId(), userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Получение списка мероприятий пользователя по роли")
    @GetMapping("{id}/events")
    public ResponseEntity<List<EventResponse>> getEventsByRole(
            @AuthenticationPrincipal UserLoginInfo userDetails,
            @Positive(message = "Параметр id не может быть меньше 1!")
            @Parameter(name = "id", description = "ID роли", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok().body(
                EventMapper.eventsToEventResponseList(eventRoleService.getEventsByRole(userDetails.getUser().getId(), id))
        );
    }
}
