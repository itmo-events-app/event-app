package org.itmo.eventapp.main.controller;

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

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(roleService.findById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @GetMapping("/organizational")
    public ResponseEntity<List<RoleResponse>> getOrganizationalRoles() {
        return ResponseEntity.ok(roleService.getOrganizational());
    }

    @GetMapping("/search")
    public ResponseEntity<List<RoleResponse>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(roleService.searchByName(name));
    }

    @PostMapping("/")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        return ResponseEntity.ok(roleService.createRole(roleRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@Min(5) @PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> editRole(@Min(5) @PathVariable Integer id,
                                                 @Valid @RequestBody RoleRequest roleRequest) {
        return ResponseEntity.ok(roleService.editRole(id, roleRequest));
    }

    @GetMapping("/system-privileges")
    public ResponseEntity<List<PrivilegeResponse>> getSystemPrivileges() {
        return ResponseEntity.ok(privilegeService.getPrivilegeByType(PrivilegeType.SYSTEM));
    }

    @GetMapping("/organizational-privileges")
    public ResponseEntity<List<PrivilegeResponse>> getOrganizationalPrivileges() {
        return ResponseEntity.ok(privilegeService.getPrivilegeByType(PrivilegeType.EVENT));
    }

    @GetMapping("/privileges")
    public ResponseEntity<List<PrivilegeResponse>> getAllPrivileges() {
        return ResponseEntity.ok(privilegeService.getAll());
    }

    @PostMapping("/organizational/{userId}/{eventId}")
    public ResponseEntity<?> assignOrganizationalRole(@Positive @PathVariable Integer userId,
                                                      @Positive @PathVariable Integer eventId,
                                                      @Min(3) @RequestBody Integer roleId) {
        eventRoleService.assignOrganizationalRole(userId, roleId, eventId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/organizer/{userId}/{eventId}")
    public ResponseEntity<?> assignOrganizerRole(@Positive @PathVariable Integer userId,
                                                 @Positive @PathVariable Integer eventId) {
        eventRoleService.assignOrganizationalRole(userId, roleService.getOrganizerRole().getId(), eventId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("assistant/{userId}/{eventId}")
    public ResponseEntity<?> assignAssistantRole(@Positive @PathVariable Integer userId,
                                                 @Positive @PathVariable Integer eventId) {
        eventRoleService.assignOrganizationalRole(userId, roleService.getAssistantRole().getId(), eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/organizational/{userId}/{eventId}")
    public ResponseEntity<?> revokeOrganizationalRole(@Positive @PathVariable Integer userId,
                                                      @Positive @PathVariable Integer eventId,
                                                      @Min(3) @RequestBody Integer roleId) {
        eventRoleService.revokeOrganizationalRole(userId, roleId, eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/organizer/{userId}/{eventId}")
    public ResponseEntity<?> revokeOrganizerRole(@Positive @PathVariable Integer userId,
                                                 @Positive @PathVariable Integer eventId) {
        eventRoleService.revokeOrganizationalRole(userId, roleService.getOrganizerRole().getId(), eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/assistant/{userId}/{eventId}")
    public ResponseEntity<?> revokeAssistantRole(@Positive @PathVariable Integer userId,
                                                 @Positive @PathVariable Integer eventId) {
        eventRoleService.revokeOrganizationalRole(userId, roleService.getAssistantRole().getId(), eventId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/system/{userId}")
    public ResponseEntity<?> assignSystemRole(@Positive @PathVariable Integer userId,
                                              @Positive @RequestBody Integer roleId) {
        roleService.assignSystemRole(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/system/{userId}")
    public ResponseEntity<?> revokeSystemRole(@Positive @PathVariable Integer userId) {
        roleService.revokeSystemRole(userId);
        return ResponseEntity.ok().build();
    }
}
