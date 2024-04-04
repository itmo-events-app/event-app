package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.UserChangeEmailRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangeNameRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangePasswordRequest;
import org.itmo.eventapp.main.model.dto.response.PrivilegeResponse;
import org.itmo.eventapp.main.model.dto.response.UserRoleResponse;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.model.mapper.EventRoleMapper;
import org.itmo.eventapp.main.model.mapper.PrivilegeMapper;
import org.itmo.eventapp.main.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserService userService;

    @PutMapping("/change-name")
    public ResponseEntity<Void> changeName(Authentication authentication, @Valid @RequestBody UserChangeNameRequest request) {
        userService.changeName(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-email")
    public ResponseEntity<Void> changeEmail(Authentication authentication, @Valid @RequestBody UserChangeEmailRequest request) {
        userService.changeEmail(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(Authentication authentication, @Valid @RequestBody UserChangePasswordRequest request) {
        userService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/event-privileges/{id}")
    public ResponseEntity<List<PrivilegeResponse>> getUserEventPrivileges(
            @AuthenticationPrincipal UserLoginInfo userDetails,
            @Min(1) @PathVariable("id") Integer id) {
        Integer userId = userDetails.getUser().getId();
        return ResponseEntity.ok(
                PrivilegeMapper.privilegesToPrivilegeResponseList(userService.getUserEventPrivileges(userId, id)));
    }
}
