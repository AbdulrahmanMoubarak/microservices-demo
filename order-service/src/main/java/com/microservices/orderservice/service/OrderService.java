package com.microservices.orderservice.service;

import com.microservices.orderservice.model.Order;
import com.microservices.orderservice.model.OrderLineItems;
import com.microservices.orderservice.model.dto.OrderRequest;
import com.microservices.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class OrderService {

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

        orderRepository.save(order);
        log.info("Order {} saved sucessfully", order.getOrderNumber());
    }
}
