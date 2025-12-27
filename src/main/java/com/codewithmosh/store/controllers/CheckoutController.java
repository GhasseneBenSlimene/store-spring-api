package com.ghassenebenslimene.store.controllers;

import com.ghassenebenslimene.store.dtos.CheckoutRequest;
import com.ghassenebenslimene.store.dtos.CheckoutResponse;
import com.ghassenebenslimene.store.dtos.ErrorDto;
import com.ghassenebenslimene.store.exceptions.CartEmptyException;
import com.ghassenebenslimene.store.exceptions.CartNotFoundException;
import com.ghassenebenslimene.store.exceptions.PaymentException;
import com.ghassenebenslimene.store.services.CheckoutService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout session"));
    }


    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
