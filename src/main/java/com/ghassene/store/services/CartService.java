package com.ghassene.store.services;

import com.ghassene.store.dtos.CartDto;
import com.ghassene.store.dtos.CartItemDto;
import com.ghassene.store.entities.Cart;
import com.ghassene.store.entities.Product;
import com.ghassene.store.exceptions.CartNotFoundException;
import com.ghassene.store.exceptions.ProductNotFoundException;
import com.ghassene.store.mappers.CartMapper;
import com.ghassene.store.repositories.CartRepository;
import com.ghassene.store.repositories.ProductRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart() {
        Cart cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId){
        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null){
            throw new CartNotFoundException();
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null){
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);

    }
}
