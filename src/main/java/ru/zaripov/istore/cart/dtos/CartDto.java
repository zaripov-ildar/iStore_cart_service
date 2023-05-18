package ru.zaripov.istore.cart.dtos;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(List<CartItemDto> cartItems, BigDecimal totalPrice) {}
