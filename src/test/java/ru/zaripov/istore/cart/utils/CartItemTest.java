package ru.zaripov.istore.cart.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    void incrementQuantity() {

        BigDecimal totalPrice = BigDecimal.TEN;
        int quantity = 10;
        CartItem item = CartItem.builder()
                .productId(1L)
                .totalPrice(totalPrice)
                .pricePerProduct(BigDecimal.ONE)
                .amount(quantity)
                .productTitle("title")
                .build();
        item.incrementQuantity();
        assertEquals(quantity + 1, item.getAmount());
        assertEquals(totalPrice.add(BigDecimal.ONE), item.getTotalPrice());
    }

}