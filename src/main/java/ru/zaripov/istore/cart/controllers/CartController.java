package ru.zaripov.istore.cart.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.zaripov.istore.cart.converters.CartConverter;
import ru.zaripov.istore.cart.dtos.CartDto;
import ru.zaripov.istore.cart.dtos.ProductsSetDto;
import ru.zaripov.istore.cart.services.CartService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;
    private final CartConverter cartConverter;


    @GetMapping("/generateId")
    public String generateId() {
        return cartService.getNewId();
    }

    @GetMapping("/{userCartId}")
    public CartDto getCurrentCart(@PathVariable String userCartId) {
        return cartConverter.toCartDto(cartService.getCurrentCart(userCartId));
    }

    @PatchMapping("/clear/{userCartId}")
    public void clear(@PathVariable String userCartId) {
        cartService.clear(userCartId);
    }

    @PatchMapping("/setProducts")
    public void setProducts(@RequestBody ProductsSetDto products) {
        cartService.setProducts(products);
        getCurrentCart(products.cartId());
    }
}
