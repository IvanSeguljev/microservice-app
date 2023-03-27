package com.iseguljev.inventoryservice.service;

import com.iseguljev.inventoryservice.dto.InventoryResponse;
import com.iseguljev.inventoryservice.model.Inventory;
import com.iseguljev.inventoryservice.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional
    public List<InventoryResponse> isInStock(List<String> skuCode){
        return inventoryRepository.findBySkuCodeIn(skuCode)
                .stream()
                .map(x-> InventoryResponse
                        .builder()
                        .skuCode(x.getSkuCode())
                        .isInStock(x.getQuantity()>0)
                        .build())
                .toList();
    }

}
