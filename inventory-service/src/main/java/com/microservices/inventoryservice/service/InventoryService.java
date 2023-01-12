package com.microservices.inventoryservice.service;

import com.microservices.inventoryservice.model.dto.InventoryResponse;
import com.microservices.inventoryservice.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes){
        log.info("received request for sku codes: {}", skuCodes);
        return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
                .map(inventoryItem -> InventoryResponse.builder()
                            .skuCode(inventoryItem.getSkuCode())
                            .isInStock(inventoryItem.getQuantity() > 0)
                            .build())
                .collect(Collectors.toList());
    }
}
