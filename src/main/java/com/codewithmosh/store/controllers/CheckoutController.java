package com.ghassenebenslimene.store.controllers;

import com.ghassenebenslimene.store.dtos.CheckoutRequest;
import com.ghassenebenslimene.store.dtos.CheckoutResponse;
import com.ghassenebenslimene.store.dtos.ErrorDto;
import com.ghassenebenslimene.store.entities.Order;
import com.ghassenebenslimene.store.repositories.CartRepository;
import com.ghassenebenslimene.store.repositories.OrderRepository;
import com.ghassenebenslimene.store.services.AuthService;
import com.ghassenebenslimene.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> checkout(
        @Valid @RequestBody CheckoutRequest request
    ) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            return ResponseEntity.badRequest().body(
                new ErrorDto("Cart not found")
            );
        }

        if (cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ErrorDto("Cart is empty")
            );
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return ResponseEntity.ok(new CheckoutResponse(order.getId()));
    }
}
