package com.ghassenebenslimene.store.mappers;

import com.ghassenebenslimene.store.dtos.UserDto;
import com.ghassenebenslimene.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
