package com.gabriel.empregos.api.auth.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.gabriel.empregos.api.auth.dtos.UserRequest;
import com.gabriel.empregos.api.auth.dtos.UserResponse;
import com.gabriel.empregos.core.models.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ModelMapperUserMapper implements UserMapper {

    private final ModelMapper modelMapper;

    @Override
    public UserResponse toUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public User toUser(UserRequest userRequest) {
        return modelMapper.map(userRequest, User.class);
    }

}
