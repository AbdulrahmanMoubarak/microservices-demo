package com.microservices.orderservice.service;

import com.microservices.orderservice.model.Order;
import com.microservices.orderservice.model.OrderLineItems;
import com.microservices.orderservice.model.dto.InventoryResponse;
import com.microservices.orderservice.model.dto.OrderLineItemsDto;
import com.microservices.orderservice.model.dto.OrderRequest;
import com.microservices.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {


    @Value("${inventory-url}")
    private String inventoryUrl;
    private final WebClient.Builder webClientBuilder;

    private final OrderRepository orderRepository;
    public void placeOrder(OrderRequest orderRequest){
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(
                        orderRequest.getOrderLineItemsDtoList().stream().map(item ->{
                            return OrderLineItems.builder()
                                    .id(item.getId())
                                    .price(item.getPrice())
                                    .quantity(item.getQuantity())
                                    .skuCode(item.getSkuCode())
                                    .build();
                        }).collect(Collectors.toList())
                )
                .build();

        if(productsInStock(orderRequest.getOrderLineItemsDtoList().stream().map(OrderLineItemsDto::getSkuCode).collect(Collectors.toList()))){
            orderRepository.save(order);
            log.info("Order {} saved sucessfully", order.getOrderNumber());
        } else {
            throw new IllegalArgumentException("One or more products are not in stock");
        }

    }

    private Boolean productsInStock(List<String> skuCodes){
        InventoryResponse[] result = webClientBuilder.build().get()
                .uri(inventoryUrl,
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        if(result == null)
            throw new RuntimeException("Error while communicating with inventory servive");
        else{
            return Arrays.stream(result).allMatch(InventoryResponse::getIsInStock);
        }
    }
}
