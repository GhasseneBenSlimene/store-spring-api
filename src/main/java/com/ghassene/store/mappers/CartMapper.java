package com.ghassene.store.mappers;

import com.ghassene.store.dtos.CartDto;
import com.ghassene.store.dtos.CartItemDto;
import com.ghassene.store.dtos.CartProductDto;
import com.ghassene.store.entities.Cart;
import com.ghassene.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
