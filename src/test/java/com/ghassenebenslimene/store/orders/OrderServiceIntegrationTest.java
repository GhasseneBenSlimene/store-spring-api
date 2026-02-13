package com.ghassenebenslimene.store.orders;

import com.ghassenebenslimene.store.auth.AuthService;
import com.ghassenebenslimene.store.users.Role;
import com.ghassenebenslimene.store.users.User;
import com.ghassenebenslimene.store.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderServiceIntegrationTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getOrderReturnsDtoForOwner() {
        var user = new User();
        user.setName("Owner");
        user.setEmail("owner@example.com");
        user.setPassword("secret");
        user.setRole(Role.USER);
        userRepository.save(user);

        var order = new Order();
        order.setCustomer(user);
        order.setStatus(PaymentStatus.PENDING);
        order.setTotalPrice(BigDecimal.valueOf(10));
        orderRepository.save(order);

        var dto = new OrderDto();
        dto.setId(order.getId());

        when(authService.getCurrentUser()).thenReturn(user);
        when(orderMapper.toDto(any(Order.class))).thenReturn(dto);

        var result = orderService.getOrder(order.getId());

        assertThat(result.getId()).isEqualTo(order.getId());
    }

    @Test
    void getOrderRejectsNonOwner() {
        var owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner.setPassword("secret");
        owner.setRole(Role.USER);
        userRepository.save(owner);

        var other = new User();
        other.setName("Other");
        other.setEmail("other@example.com");
        other.setPassword("secret");
        other.setRole(Role.USER);
        userRepository.save(other);

        var order = new Order();
        order.setCustomer(owner);
        order.setStatus(PaymentStatus.PENDING);
        order.setTotalPrice(BigDecimal.valueOf(10));
        orderRepository.save(order);

        when(authService.getCurrentUser()).thenReturn(other);

        assertThatThrownBy(() -> orderService.getOrder(order.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }
}
