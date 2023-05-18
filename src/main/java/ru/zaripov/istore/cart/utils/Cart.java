package ru.zaripov.istore.cart.utils;

import lombok.Data;
import ru.zaripov.istore.cart.dtos.ProductDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {

    private List<CartItem> items;
    private BigDecimal totalPrice;

    public void clear() {
        items.clear();
        totalPrice = BigDecimal.ZERO;
    }

    private void recalculate() {
        totalPrice = BigDecimal.ZERO;
        items.forEach(i -> totalPrice = totalPrice.add(i.getTotalPrice()));
    }

    public Cart() {
        this.items = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    public void remove(Long productId) {
        if (items.removeIf(p -> p.getProductId().equals(productId))) {
            recalculate();
        }
    }

    public void setProducts(ProductDto product, Integer amount) {
        if (amount <= 0) {
            remove(product.id());
            return;
        }
        for (CartItem item : items) {
            if (item.getProductId().equals(product.id())) {
                item.setAmount(amount);
                recalculate();
                return;
            }
        }
        BigDecimal totalPrice = product.price().multiply(BigDecimal.valueOf(amount));
        CartItem item = CartItem.builder()
                .productTitle(product.title())
                .productId(product.id())
                .pricePerProduct(product.price())
                .totalPrice(totalPrice)
                .amount(amount)
                .build();
        items.add(item);
        recalculate();
    }
}
