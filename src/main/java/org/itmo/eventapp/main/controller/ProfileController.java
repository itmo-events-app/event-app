package org.itmo.eventapp.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.NotificationSettingsRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangeLoginRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangeNameRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangePasswordRequest;
import org.itmo.eventapp.main.model.dto.response.*;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.model.mapper.PrivilegeMapper;
import org.itmo.eventapp.main.model.mapper.UserMapper;
import org.itmo.eventapp.main.service.EventRoleService;
import org.itmo.eventapp.main.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserService userService;

    private final EventRoleService eventRoleService;

    @Operation(summary = "Получение информации о текущем пользователе")
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getUserInfo(Authentication authentication) {
        String login = authentication.getName();
        User user = userService.findByLogin(login);

        return ResponseEntity.ok(new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                Collections.singletonList(new UserInfoResponse(login, user.getUserLoginInfo().getLoginType())),
                user.getUserLoginInfo().getLastLoginDate(),
                user.getUserNotificationInfo().isEnablePushNotifications(),
                user.getUserNotificationInfo().isEnableEmailNotifications(),
                user.getUserNotificationInfo().getDevices()
        ));
    }

    @Operation(summary = "Обновление настроек уведомлений")
    @PutMapping("/notifications")
    public ResponseEntity<Void> updateNotifications(Authentication authentication, @RequestBody NotificationSettingsRequest request) {
        userService.updateNotifications(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Смена имени пользователя")
    @PutMapping("/name")
    public ResponseEntity<Void> changeName(Authentication authentication, @Valid @RequestBody UserChangeNameRequest request) {
        userService.changeName(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Смена логина пользователя")
    @PutMapping("/login")
    public ResponseEntity<Void> changeLogin(Authentication authentication, @Valid @RequestBody UserChangeLoginRequest request) {
        userService.changeLogin(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Смена пароля пользователя")
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(Authentication authentication, @Valid @RequestBody UserChangePasswordRequest request) {
        userService.changePassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получение списка всех привилегий пользователя в данном мероприятии")
    @GetMapping("/event-privileges/{id}")
    public ResponseEntity<List<PrivilegeResponse>> getUserEventPrivileges(
            @AuthenticationPrincipal UserLoginInfo userDetails,
            @Min(value = 1, message = "Поле eventId не может быть меньше 1!") @PathVariable("id") @Parameter(name = "id", description = "ID мероприятия", example = "1") Integer id) {
        Integer userId = userDetails.getUser().getId();
        return ResponseEntity.ok(
                PrivilegeMapper.privilegesToPrivilegeResponseList(eventRoleService.getUserEventPrivileges(userId, id)));
    }

    @Operation(summary = "Получение списка системных привилегий пользователя")
    @GetMapping("/system-privileges")
    public ResponseEntity<PrivilegeWithHasOrganizerRolesResponse> getUserSystemPrivileges(
            @AuthenticationPrincipal UserLoginInfo userDetails) {
        Integer userId = userDetails.getUser().getId();
        Set<Privilege> privilegeSet = userService.getUserSystemPrivileges(userId);
        boolean userHasOrganizerRoles = eventRoleService.userHasOrganizerRoles(userId);

        return ResponseEntity.ok().body(
                new PrivilegeWithHasOrganizerRolesResponse(
                        PrivilegeMapper.privilegesToPrivilegeResponseList(privilegeSet),
                        userHasOrganizerRoles
                ));
    }

    @Operation(summary = "Получение списка пользователей в системе")
    @GetMapping("/all-system-users")
    public ResponseEntity<PaginatedResponse<UserSystemRoleResponse>> getAllUsers(
            @RequestParam(name = "searchQuery", defaultValue = "")
                @Parameter(name = "searchQuery",
                        description = "Строка для поиска по имени и фамилии",
                        example = "Иван Иванов")
                String searchQuery,
            @RequestParam(name = "page", defaultValue = "0")
                @Min(0)
                @Parameter(name = "page",
                        description = "Номер страницы, с которой начать показ пользователей",
                        example = "0")
                Integer page,
            @RequestParam(name = "size", defaultValue = "10")
                @Min(1)
                @Max(25)
                @Parameter(name = "size",
                        description = "Число пользователей на странице",
                        example = "15")
                Integer size
    ) {
        Page<User> pages = userService.getAllFilteredUsers(searchQuery, page, size);
        PaginatedResponse<UserSystemRoleResponse> response =
                new PaginatedResponse<>(pages.getTotalElements(), pages.getContent()
                        .stream().map(UserMapper::userToUserSystemRoleResponse).toList());
        return ResponseEntity.ok().body(response);
    }
}
