package com.iseguljev.inventoryservice;

import com.iseguljev.inventoryservice.model.Inventory;
import com.iseguljev.inventoryservice.repository.InventoryRepository;
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
			Inventory inventory = new Inventory();
			inventory.setQuantity(5);
			inventory.setSkuCode("Iphone_13");

			Inventory inventory1 = new Inventory();
			inventory1.setQuantity(10);
			inventory1.setSkuCode("Iphone_12");

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}

}
