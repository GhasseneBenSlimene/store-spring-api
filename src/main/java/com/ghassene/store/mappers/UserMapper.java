package com.ghassene.store.mappers;

import com.ghassene.store.Dtos.UserDto;
import com.ghassene.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
