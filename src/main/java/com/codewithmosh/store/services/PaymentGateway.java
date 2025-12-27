package com.ghassenebenslimene.store.services;

import com.ghassenebenslimene.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
}
