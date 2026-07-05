package com.gabriel.empregos.api.auth.mappers;

import com.gabriel.empregos.api.auth.dtos.UserRequest;
import com.gabriel.empregos.api.auth.dtos.UserResponse;
import com.gabriel.empregos.core.models.User;

public interface UserMapper {

    UserResponse toUserResponse(User user);
    User toUser(UserRequest userRequest);

}
