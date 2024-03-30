package org.itmo.eventapp.main.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.response.RoleResponse;
import org.itmo.eventapp.main.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@Min(5) @PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
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
}
