package com.ghassenebenslimene.store.mappers;

import com.ghassenebenslimene.store.dtos.OrderDto;
import com.ghassenebenslimene.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
