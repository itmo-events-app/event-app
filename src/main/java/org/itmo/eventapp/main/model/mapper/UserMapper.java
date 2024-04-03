package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.response.UserShortDataResponse;
import org.itmo.eventapp.main.model.entity.User;

public class UserMapper {

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
}
