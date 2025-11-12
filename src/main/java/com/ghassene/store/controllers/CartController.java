package com.ghassene.store.controllers;

import com.ghassene.store.dtos.AddItemToCartRequest;
import com.ghassene.store.dtos.CartDto;
import com.ghassene.store.dtos.CartItemDto;
import com.ghassene.store.dtos.UpdateCartItemRequest;
import com.ghassene.store.entities.Cart;
import com.ghassene.store.entities.CartItem;
import com.ghassene.store.entities.Product;
import com.ghassene.store.exceptions.CartNotFoundException;
import com.ghassene.store.exceptions.ProductNotFoundException;
import com.ghassene.store.mappers.CartMapper;
import com.ghassene.store.repositories.CartRepository;
import com.ghassene.store.repositories.ProductRepository;
import com.ghassene.store.services.CartService;
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
    private final CartService cartService;


    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ){
        var cartdto = cartService.createCart();

        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartdto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartdto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(@PathVariable UUID cartId,
                                                 @RequestBody AddItemToCartRequest request){
        var cartItemDto = cartService.addToCart(cartId, request.getProductId());

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

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Product not found in cart")
        );
    }
}
