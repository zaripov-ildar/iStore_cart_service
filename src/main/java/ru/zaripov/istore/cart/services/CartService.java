package ru.zaripov.istore.cart.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.zaripov.istore.cart.dtos.ProductDto;
import ru.zaripov.istore.cart.dtos.ProductsSetDto;
import ru.zaripov.istore.cart.exceptions.ResourceNotFoundException;
import ru.zaripov.istore.cart.integrations.ProductServiceIntegration;
import ru.zaripov.istore.cart.utils.Cart;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CartService {

    @Value("${cart.expiration_seconds}")
    private Integer EXPIRATION_SECONDS;

    private final ProductServiceIntegration productServiceIntegration;

    private final RedisTemplate<String, Object> redisTemplate;

    public Cart getCurrentCart(String cartId) {
        Optional<Object> cart = Optional.ofNullable(redisTemplate.opsForValue().get(cartId));
        return (Cart) cart.orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Cart id '%s' doesn't exist", cart)));
    }

    public void clear(String cartId) {
        execute(cartId, Cart::clear);
    }

    private void execute(String cartId, Consumer<Cart> action) {
        Cart cart = getCurrentCart(cartId);
        action.accept(cart);
        redisTemplate.opsForValue().set(cartId, cart, Duration.ofSeconds(EXPIRATION_SECONDS));
    }

    public String getNewId() {
        String uuid;
        while (true) {
            uuid = UUID.randomUUID().toString();
            if (!redisTemplate.hasKey(uuid)) {
                redisTemplate.opsForValue().setIfAbsent(uuid, new Cart(), Duration.ofSeconds(EXPIRATION_SECONDS));
                return uuid;
            }
        }
    }

    public void setProducts(ProductsSetDto products) {
        execute(products.cartId(), cart -> {
            ProductDto product = productServiceIntegration.getProductDtoById(products.productId());
            cart.setProducts(product, products.amount());
        });
    }
}