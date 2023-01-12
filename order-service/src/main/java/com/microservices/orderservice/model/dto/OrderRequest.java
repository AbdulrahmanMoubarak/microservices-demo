package com.microservices.orderservice.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    List<OrderLineItemsDto> orderLineItemsDtoList;
}
