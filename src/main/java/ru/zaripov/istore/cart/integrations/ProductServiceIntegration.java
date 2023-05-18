package ru.zaripov.istore.cart.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.zaripov.istore.cart.dtos.ProductDto;
import ru.zaripov.istore.cart.exceptions.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
public class ProductServiceIntegration {
    private final WebClient productWebClient;


    public ProductDto getProductDtoById(Long productId) {
        return productWebClient
                .get()
                .uri("/api/v1/" + productId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(ResourceNotFoundException.class)
                                .map(body -> new ResourceNotFoundException("Something wrong on product service: " + clientResponse.statusCode())) //FIXME: handle all possible exceptions
                )
                .bodyToMono(ProductDto.class)
                .block();

    }
}
