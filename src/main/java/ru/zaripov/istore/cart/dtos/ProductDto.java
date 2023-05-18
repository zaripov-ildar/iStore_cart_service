package ru.zaripov.istore.cart.dtos;

import java.math.BigDecimal;

public record ProductDto(Long id, String title, BigDecimal price, String categoryTitle) {
}
