package com.iseguljev.orderservice.service;

import com.iseguljev.orderservice.dto.InventoryResponse;
import com.iseguljev.orderservice.dto.OrderLineItemsDto;
import com.iseguljev.orderservice.dto.OrderRequest;
import com.iseguljev.orderservice.model.Order;
import com.iseguljev.orderservice.model.OrderLineItems;
import com.iseguljev.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemsList(orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList());

       InventoryResponse[] responseArray =  webClient.get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("sku-code",order.getOrderLineItemsList()).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

       boolean allProductsInStock = Arrays.stream(responseArray).allMatch(InventoryResponse::getIsInStock);

       if(allProductsInStock) {
           orderRepository.save(order);
           log.info("Order {} successfully inserted", order.getOrderNumber());
       } else {
           throw new IllegalArgumentException();
       }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        return orderLineItems;
    }
}
