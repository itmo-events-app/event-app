package org.itmo.eventapp.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.RoleRequest;
import org.itmo.eventapp.main.model.dto.response.PrivilegeResponse;
import org.itmo.eventapp.main.model.dto.response.RoleResponse;
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
    public ResponseEntity<?> deleteRole(@Min(5) @PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Изменение роли")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> editRole(@Min(5) @PathVariable Integer id, @Valid @RequestBody RoleRequest roleRequest) {
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
        return ResponseEntity.ok(privilegeService.getSystem());
    }

    @Operation(summary = "Получение списка организационных привилегий")
    @GetMapping("/organizational-privileges")
    public ResponseEntity<List<PrivilegeResponse>> getOrganizationalPrivileges() {
        return ResponseEntity.ok(privilegeService.getOrganizational());
    }
}
