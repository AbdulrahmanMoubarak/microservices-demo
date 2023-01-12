package com.microservices.inventoryservice;

import com.microservices.inventoryservice.model.InventoryItem;
import com.microservices.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			inventoryRepository.save(
					InventoryItem.builder()
							.skuCode("iphone_13")
							.quantity(100)
							.build()
			);
			inventoryRepository.save(
					InventoryItem.builder()
							.skuCode("iphone_13_red")
							.quantity(0)
							.build()
			);
		};
	}

}
