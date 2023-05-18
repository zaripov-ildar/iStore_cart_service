package ru.zaripov.istore.cart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.zaripov.istore.cart.dtos.ProductDto;
import ru.zaripov.istore.cart.dtos.ProductsSetDto;
import ru.zaripov.istore.cart.integrations.ProductServiceIntegration;
import ru.zaripov.istore.cart.utils.Cart;

import java.math.BigDecimal;
import java.time.Duration;

import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ValueOperations<String, Object> valueOperations;

    @MockBean
    private RedisTemplate<String, Object> redisTemplate;

    @MockBean
    private ProductServiceIntegration productServiceIntegration;

    @MockBean
    private JedisConnectionFactory factory;

    @MockBean
    private RedisConnection connection;

    @MockBean
    private RedisKeyValueAdapter adapter;

    private final String url = "/api/v1/cart";
    private final String cartId = "cartId";
    private ProductDto product;
    private Cart cart;

    @BeforeEach
    void init() {
        long productId = 1L;
        product = new ProductDto(productId, "product", BigDecimal.valueOf(42), "category");
        cart = new Cart();

        given(productServiceIntegration.getProductDtoById(product.id()))
                .willReturn(product);

        cart.setProducts(product, 1);


        given(redisTemplate.opsForValue())
                .willReturn(valueOperations);
        given(valueOperations.get(cartId))
                .willReturn(cart);

    }


    @Test
    void getCurrentCart() throws Exception {
        mockMvc.perform(get(url + "/" + cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice", is(42)));
    }

    @Test
    void badRequestOnNotExistingCartId() throws Exception {
        mockMvc.perform(get(url + "/notExistingId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void setProducts() throws Exception {
        ProductsSetDto dto = new ProductsSetDto(product.id(), 2, cartId);
        String body = (new ObjectMapper()).writeValueAsString(dto);
        ArgumentCaptor<Cart> argument = ArgumentCaptor.forClass(Cart.class);

        mockMvc.perform(patch(url + "/setProducts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(valueOperations, times(1)).set(anyString(), argument.capture(), any(Duration.class));
        BigDecimal newTotalPrice = product.price().multiply(BigDecimal.valueOf(dto.amount()));
        assertEquals(newTotalPrice, argument.getValue().getTotalPrice());
    }

    @Test
    void removeProductFromCart() throws Exception {
        ProductsSetDto dto = new ProductsSetDto(product.id(), 0, cartId);
        String body = (new ObjectMapper()).writeValueAsString(dto);
        ArgumentCaptor<Cart> argument = ArgumentCaptor.forClass(Cart.class);

        mockMvc.perform(patch(url + "/setProducts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(valueOperations, times(1))
                .set(anyString(), argument.capture(), any(Duration.class));

        assertEquals(0, argument.getValue().getItems().size());
    }

    @Test
    void clear() throws Exception {
        mockMvc.perform(patch(url + String.format("/clear/%s", cartId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(valueOperations, times(1)).set(anyString(), any(Object.class), any(Duration.class));
    }

    @Test
    void generateId() throws Exception {
        given(redisTemplate.hasKey(anyString())).willReturn(false);
        mockMvc.perform(get(url + "/generateId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasLength(36)));
    }
}