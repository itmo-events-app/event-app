package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.response.UserShortDataResponse;
import org.itmo.eventapp.main.model.dto.response.UserSystemRoleResponse;
import org.itmo.eventapp.main.model.entity.User;

import java.util.List;
import java.util.stream.Collectors;

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

    public static UserSystemRoleResponse userToUserSystemRoleResponse(User user) {
        return new UserSystemRoleResponse(
                // getUserLoginInfo is always not null
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getUserLoginInfo().getLogin(),
                user.getUserLoginInfo().getLoginType(),
                user.getRole().getName()
        );
    }

    public static List<UserSystemRoleResponse> usersToUserSystemRoleResponses(List<User> users) {
        return users.stream()
                .map(UserMapper::userToUserSystemRoleResponse)
                .toList();
    }
}
