## Product Server

This project implements a cart service using Spring Boot 3.0 and integration with other servers using webflux.  It includes the following features:

### Features
* Storing user carts in Redis  
* Generating a unique cart key (cartId)
* Limitation carts keys lifetime
* Search cart by cartId
* Cleaning the cart by key
* Setting values of cart items  (product title, price, amount, etc.)
* 
* All endpoints protected by <a href="">gateway server</a> according users roles and authorities
* Project covered by tests for 82% lines and 94% of the remaining classes

### Technologies
* Spring Boot 3.0.6
* Spring-boot-starter-webflux for integration with <a href="https://github.com/zaripov-ildar/iStore_product_service">product server</a>
* Redis
* Mockito
* JUnit5
* Maven
