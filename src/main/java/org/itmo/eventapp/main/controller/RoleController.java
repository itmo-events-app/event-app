package org.itmo.eventapp.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.RoleRequest;
import org.itmo.eventapp.main.model.dto.response.PrivilegeResponse;
import org.itmo.eventapp.main.model.dto.response.RoleResponse;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeType;
import org.itmo.eventapp.main.service.EventRoleService;
import org.itmo.eventapp.main.service.PrivilegeService;
import org.itmo.eventapp.main.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final PrivilegeService privilegeService;
    private final EventRoleService eventRoleService;

    @Operation(summary = "Получение роли по id")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(roleService.findById(id));
    }

    @Operation(summary = "Получение списка всех ролей")
    @GetMapping("/")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @Operation(summary = "Получение организационных ролей")
    @GetMapping("/organizational")
    public ResponseEntity<List<RoleResponse>> getOrganizationalRoles() {
        return ResponseEntity.ok(roleService.getOrganizational());
    }

    @Operation(summary = "Поиск ролей по названию")
    @GetMapping("/search")
    public ResponseEntity<List<RoleResponse>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(roleService.searchByName(name));
    }

    @Operation(summary = "Создание роли")
    @PostMapping("/")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        return ResponseEntity.ok(roleService.createRole(roleRequest));
    }

    @Operation(summary = "Удаление роли")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteRole(@Min(5) @PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Редактирование роли")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> editRole(@Min(5) @PathVariable Integer id,
                                                 @Valid @RequestBody RoleRequest roleRequest) {
        return ResponseEntity.ok(roleService.editRole(id, roleRequest));
    }

    @Operation(summary = "Получение списка всех привилегий")
    @GetMapping("/privileges")
    public ResponseEntity<List<PrivilegeResponse>> getAllPrivileges() {
        return ResponseEntity.ok(privilegeService.getAll());
    }

    @Operation(summary = "Получение списка системных привилегий")
    @GetMapping("/system-privileges")
    public ResponseEntity<List<PrivilegeResponse>> getSystemPrivileges() {
        return ResponseEntity.ok(privilegeService.getPrivilegeByType(PrivilegeType.SYSTEM));
    }

    @Operation(summary = "Получение списка организационных привилегий")
    @GetMapping("/organizational-privileges")
    public ResponseEntity<List<PrivilegeResponse>> getOrganizationalPrivileges() {
        return ResponseEntity.ok(privilegeService.getPrivilegeByType(PrivilegeType.EVENT));
    }


    @Operation(summary = "Назначение пользователю организационной роли")
    @PostMapping("/organizational/{userId}/{eventId}")
    public ResponseEntity<Integer> assignOrganizationalRole(@Positive @PathVariable Integer userId,
                                                      @Positive @PathVariable Integer eventId,
                                                      @Min(3) @RequestBody Integer roleId) {
        eventRoleService.assignOrganizationalRole(userId, roleId, eventId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Назначение пользователю роли Организатор")
    @PostMapping("/organizer/{userId}/{eventId}")
    public ResponseEntity<?> assignOrganizerRole(@Positive @PathVariable Integer userId,
                                                 @Positive @PathVariable Integer eventId) {
        eventRoleService.assignOrganizationalRole(userId, roleService.getOrganizerRole().getId(), eventId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Назначение пользователю роли Помощник")
    @PostMapping("assistant/{userId}/{eventId}")
    public ResponseEntity<Integer> assignAssistantRole(@Positive @PathVariable Integer userId,
                                                 @Positive @PathVariable Integer eventId) {
        eventRoleService.assignOrganizationalRole(userId, roleService.getAssistantRole().getId(), eventId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Лишение пользователя организационной роли")
    @DeleteMapping("/organizational/{userId}/{eventId}")
    public ResponseEntity<Integer> revokeOrganizationalRole(@Positive @PathVariable Integer userId,
                                                      @Positive @PathVariable Integer eventId,
                                                      @Min(3) @RequestBody Integer roleId) {
        eventRoleService.revokeOrganizationalRole(userId, roleId, eventId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Лишение пользователя роли Организатор")
    @DeleteMapping("/organizer/{userId}/{eventId}")
    public ResponseEntity<?> revokeOrganizerRole(@Positive @PathVariable Integer userId,
                                                 @Positive @PathVariable Integer eventId) {
        eventRoleService.revokeOrganizationalRole(userId, roleService.getOrganizerRole().getId(), eventId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Лишение пользователя роли Помощник")
    @DeleteMapping("/assistant/{userId}/{eventId}")
    public ResponseEntity<Integer> revokeAssistantRole(@Positive @PathVariable Integer userId,
                                                 @Positive @PathVariable Integer eventId) {
        eventRoleService.revokeOrganizationalRole(userId, roleService.getAssistantRole().getId(), eventId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Назначение пользователю системной роли")
    @PostMapping("/system/{userId}")
    public ResponseEntity<Integer> assignSystemRole(@Positive @PathVariable Integer userId,
                                              @Positive @RequestBody Integer roleId) {
        roleService.assignSystemRole(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Лишение пользователя системной роли")
    @PutMapping("/system/{userId}")
    public ResponseEntity<Integer> revokeSystemRole(@Positive @PathVariable Integer userId) {
        roleService.revokeSystemRole(userId);
        return ResponseEntity.ok().build();
    }
}
