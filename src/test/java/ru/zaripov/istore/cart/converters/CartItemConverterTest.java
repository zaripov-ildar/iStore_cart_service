package ru.zaripov.istore.cart.converters;

import org.junit.jupiter.api.Test;
import ru.zaripov.istore.cart.dtos.CartItemDto;
import ru.zaripov.istore.cart.utils.CartItem;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartItemConverterTest {

    @Test
    void toCartItemDto() {
        CartItemConverter converter = new CartItemConverter();
        CartItem item = CartItem.builder()
                .productId(1L)
                .totalPrice(BigDecimal.TEN)
                .pricePerProduct(BigDecimal.ONE)
                .amount(10)
                .productTitle("title")
                .build();
        CartItemDto dto = converter.toCartItemDto(item);
        assertEquals(item.getProductId(), dto.productId());
        assertEquals(item.getAmount(), dto.quantity());
        assertEquals(item.getTotalPrice(), dto.totalPrice());
        assertEquals(item.getPricePerProduct(), dto.pricePerProduct());
        assertEquals(item.getProductTitle(), dto.productTitle());
    }
}