package com.ghassenebenslimene.store.mappers;

import com.ghassenebenslimene.store.dtos.CartDto;
import com.ghassenebenslimene.store.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
