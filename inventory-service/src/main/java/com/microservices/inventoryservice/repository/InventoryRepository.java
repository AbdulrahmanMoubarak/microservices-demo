package com.microservices.inventoryservice.repository;

import com.microservices.inventoryservice.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

    List<InventoryItem> findBySkuCodeIn(List<String> skuCode);
}
