package ru.zaripov.istore.cart.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.zaripov.istore.cart.dtos.CartDto;
import ru.zaripov.istore.cart.utils.Cart;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartConverter {
    private final CartItemConverter cartItemConverter;

    public CartDto toCartDto(Cart currentCart) {
        return new CartDto(
                currentCart.getItems()
                        .stream()
                        .map(cartItemConverter::toCartItemDto)
                        .toList(),
                currentCart.getTotalPrice()
        );
    }
}
