package ru.zaripov.istore.cart.converters;

import org.springframework.stereotype.Component;
import ru.zaripov.istore.cart.dtos.CartItemDto;
import ru.zaripov.istore.cart.utils.CartItem;

@Component
public class CartItemConverter {
    public CartItemDto toCartItemDto(CartItem cartItem) {
        return new CartItemDto(
                cartItem.getProductId(),
                cartItem.getProductTitle(),
                cartItem.getPricePerProduct(),
                cartItem.getTotalPrice(),
                cartItem.getAmount()
        );
    }
}
