package org.itmo.eventapp.main.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exception.NotFoundException;
import org.itmo.eventapp.main.model.dto.RoleDto;
import org.itmo.eventapp.main.service.RoleService;
import org.springframework.http.HttpStatus;
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
        try {
            roleService.deleteRole(id);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @GetMapping("/allowed")
    public ResponseEntity<List<RoleDto>> getAllowedRoles() {
        return ResponseEntity.ok(roleService.getAllowed());
    }
}
