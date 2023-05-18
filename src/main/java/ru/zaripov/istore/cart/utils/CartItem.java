package ru.zaripov.istore.cart.utils;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Long productId;
    private String productTitle;
    private int amount;
    private BigDecimal pricePerProduct;
    private BigDecimal totalPrice;

    public void incrementQuantity() {
        amount++;
        totalPrice = totalPrice.add(pricePerProduct);
    }

    public void setAmount(int amount) {
        this.amount = amount;
        totalPrice = pricePerProduct.multiply(BigDecimal.valueOf(amount));
    }
}
