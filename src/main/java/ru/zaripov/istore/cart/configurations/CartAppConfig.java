package ru.zaripov.istore.cart.configurations;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.zaripov.istore.cart.properties.ProductServiceIntegrationProperties;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(
        ProductServiceIntegrationProperties.class
)
@RequiredArgsConstructor
public class CartAppConfig {
    private final ProductServiceIntegrationProperties productSIP;

    @Bean
    public WebClient productWebServiceClient(){
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, productSIP.getConnectTimeout())
                .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(productSIP.getReadTimeout(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(productSIP.getWriteTimeout(), TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(productSIP.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
