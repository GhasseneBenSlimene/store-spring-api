package com.ghassenebenslimene.store.services;

import com.ghassenebenslimene.store.dtos.CheckoutRequest;
import com.ghassenebenslimene.store.dtos.CheckoutResponse;
import com.ghassenebenslimene.store.entities.Order;
import com.ghassenebenslimene.store.exceptions.CartEmptyException;
import com.ghassenebenslimene.store.exceptions.CartNotFoundException;
import com.ghassenebenslimene.store.repositories.CartRepository;
import com.ghassenebenslimene.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId());
    }
}
