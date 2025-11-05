package com.ghassene.store.mappers;

import com.ghassene.store.dtos.RegisterUserRequest;
import com.ghassene.store.dtos.UpdateUserRequest;
import com.ghassene.store.dtos.UserDto;
import com.ghassene.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
