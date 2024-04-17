package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.response.UserShortDataResponse;
import org.itmo.eventapp.main.model.dto.response.UserResponse;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.User;

import java.util.List;
import java.util.Map;

public class UserMapper {

    private UserMapper() {

    }

    public static UserShortDataResponse userToUserShortResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserShortDataResponse(
            user.getId(),
            user.getName(),
            user.getSurname()
        );
    }

    public static UserResponse userToUserResponse(User user, Map<String, List<String>> rolesByEvent) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getSurname(),
            user.getUserLoginInfo().getLogin(),
            user.getUserLoginInfo().getLoginType(),
            user.getRoles().stream()
                    .map(Role::getName)
                    .toList(),
            rolesByEvent
        );
    }
}
