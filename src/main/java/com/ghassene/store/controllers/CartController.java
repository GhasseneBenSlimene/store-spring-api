package com.ghassene.store.controllers;

import com.ghassene.store.dtos.AddItemToCartRequest;
import com.ghassene.store.dtos.CartDto;
import com.ghassene.store.dtos.CartItemDto;
import com.ghassene.store.dtos.UpdateCartItemRequest;
import com.ghassene.store.entities.Cart;
import com.ghassene.store.entities.CartItem;
import com.ghassene.store.entities.Product;
import com.ghassene.store.mappers.CartMapper;
import com.ghassene.store.repositories.CartRepository;
import com.ghassene.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;


    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ){
        Cart cart = new Cart();

        cartRepository.save(cart);

        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();
        return ResponseEntity.created(uri).body(cartMapper.toDto(cart));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(@PathVariable UUID cartId,
                                                 @RequestBody AddItemToCartRequest request){
        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null){
            return ResponseEntity.notFound().build();
        }

        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null){
            return ResponseEntity.badRequest().build();
        }

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);

        CartItemDto cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId){
        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCart(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request){

        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found")
            );
        }

        var cartItem = cart.getItem(productId);
        if (cartItem == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Product not found in cart")
            );
        }
        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItemFromCart(
            @PathVariable UUID cartId,
            @PathVariable Long productId){

        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found")
            );
        }

        cart.removeItem(productId);

        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(
            @PathVariable UUID cartId){

        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null){
            return ResponseEntity.notFound().build();
        }

        cart.clear();

        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }
}
