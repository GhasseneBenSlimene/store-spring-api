package com.ghassenebenslimene.store.payments;

import com.ghassenebenslimene.store.auth.AuthService;
import com.ghassenebenslimene.store.carts.Cart;
import com.ghassenebenslimene.store.carts.CartEmptyException;
import com.ghassenebenslimene.store.carts.CartNotFoundException;
import com.ghassenebenslimene.store.carts.CartRepository;
import com.ghassenebenslimene.store.carts.CartService;
import com.ghassenebenslimene.store.orders.Order;
import com.ghassenebenslimene.store.orders.OrderRepository;
import com.ghassenebenslimene.store.products.Product;
import com.ghassenebenslimene.store.users.Role;
import com.ghassenebenslimene.store.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AuthService authService;

    @Mock
    private CartService cartService;

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private CheckoutService checkoutService;

    @Test
    void checkoutThrowsWhenCartMissing() {
        var request = new CheckoutRequest();
        request.setCartId(UUID.randomUUID());

        when(cartRepository.getCartWithItems(eq(request.getCartId()))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> checkoutService.checkout(request))
                .isInstanceOf(CartNotFoundException.class);
    }

    @Test
    void checkoutThrowsWhenCartEmpty() {
        var request = new CheckoutRequest();
        request.setCartId(UUID.randomUUID());

        var cart = new Cart();
        cart.setId(request.getCartId());

        when(cartRepository.getCartWithItems(eq(request.getCartId()))).thenReturn(Optional.of(cart));

        assertThatThrownBy(() -> checkoutService.checkout(request))
                .isInstanceOf(CartEmptyException.class);
    }

    @Test
    void checkoutCreatesOrderAndClearsCart() {
        var request = new CheckoutRequest();
        request.setCartId(UUID.randomUUID());

        var cart = TestData.cartWithItem(request.getCartId());
        var user = new User();
        user.setId(5L);
        user.setRole(Role.USER);

        when(cartRepository.getCartWithItems(eq(request.getCartId()))).thenReturn(Optional.of(cart));
        when(authService.getCurrentUser()).thenReturn(user);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            var order = (Order) invocation.getArgument(0);
            order.setId(99L);
            return order;
        });
        when(paymentGateway.createCheckoutSession(any(Order.class)))
                .thenReturn(new CheckoutSession("http://checkout"));

        var response = checkoutService.checkout(request);

        assertThat(response.getOrderId()).isEqualTo(99L);
        assertThat(response.getCheckoutUrl()).isEqualTo("http://checkout");
        verify(cartService).clearCart(eq(request.getCartId()));
        verify(orderRepository, never()).delete(any(Order.class));
    }

    @Test
    void checkoutDeletesOrderWhenPaymentFails() {
        var request = new CheckoutRequest();
        request.setCartId(UUID.randomUUID());

        var cart = TestData.cartWithItem(request.getCartId());
        var user = new User();
        user.setId(5L);
        user.setRole(Role.USER);

        when(cartRepository.getCartWithItems(eq(request.getCartId()))).thenReturn(Optional.of(cart));
        when(authService.getCurrentUser()).thenReturn(user);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentGateway.createCheckoutSession(any(Order.class)))
                .thenThrow(new PaymentException("stripe error"));

        assertThatThrownBy(() -> checkoutService.checkout(request))
                .isInstanceOf(PaymentException.class);

        var orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).delete(orderCaptor.capture());
        verify(cartService, never()).clearCart(any());
        assertThat(orderCaptor.getValue().getStatus()).isNotNull();
    }

    private static final class TestData {
        private static Cart cartWithItem(UUID cartId) {
            var cart = new Cart();
            cart.setId(cartId);

            var product = new Product();
            product.setId(1L);
            product.setPrice(BigDecimal.valueOf(25));

            var item = new com.ghassenebenslimene.store.carts.CartItem();
            item.setProduct(product);
            item.setQuantity(2);
            item.setCart(cart);

            cart.getItems().add(item);
            return cart;
        }
    }
}
