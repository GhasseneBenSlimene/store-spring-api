package com.ghassenebenslimene.store.mappers;

import com.ghassenebenslimene.store.dtos.RegisterUserRequest;
import com.ghassenebenslimene.store.dtos.UpdateUserRequest;
import com.ghassenebenslimene.store.dtos.UserDto;
import com.ghassenebenslimene.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
