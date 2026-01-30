package com.ghassenebenslimene.store.orders;

import com.ghassenebenslimene.store.auth.AuthService;
import com.ghassenebenslimene.store.users.Role;
import com.ghassenebenslimene.store.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private AuthService authService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void getAllOrdersReturnsMappedDtos() {
        var user = new User();
        user.setId(1L);
        user.setRole(Role.USER);

        var order = new Order();
        order.setId(10L);
        order.setCustomer(user);

        var dto = new OrderDto();
        dto.setId(10L);

        when(authService.getCurrentUser()).thenReturn(user);
        when(orderRepository.getOrdersByCustomer(eq(user))).thenReturn(List.of(order));
        when(orderMapper.toDto(eq(order))).thenReturn(dto);

        var result = orderService.getAllOrders();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(10L);
    }

    @Test
    void getOrderThrowsWhenUserNotOwner() {
        var owner = new User();
        owner.setId(1L);
        owner.setRole(Role.USER);

        var otherUser = new User();
        otherUser.setId(2L);
        otherUser.setRole(Role.USER);

        var order = new Order();
        order.setId(50L);
        order.setCustomer(owner);

        when(orderRepository.getOrderWithItems(eq(50L))).thenReturn(Optional.of(order));
        when(authService.getCurrentUser()).thenReturn(otherUser);

        assertThatThrownBy(() -> orderService.getOrder(50L))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void getOrderReturnsMappedDto() {
        var user = new User();
        user.setId(3L);
        user.setRole(Role.USER);

        var order = new Order();
        order.setId(99L);
        order.setCustomer(user);

        var dto = new OrderDto();
        dto.setId(99L);

        when(orderRepository.getOrderWithItems(eq(99L))).thenReturn(Optional.of(order));
        when(authService.getCurrentUser()).thenReturn(user);
        when(orderMapper.toDto(eq(order))).thenReturn(dto);

        var result = orderService.getOrder(99L);

        assertThat(result.getId()).isEqualTo(99L);
    }
}
