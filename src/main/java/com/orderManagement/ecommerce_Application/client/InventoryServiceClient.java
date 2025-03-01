package com.orderManagement.ecommerce_Application.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service" , url = "http://localhost:8084/inventory")
public interface InventoryServiceClient {

    @GetMapping("/stock")
    ResponseEntity<Integer> getStock(@RequestParam("sku") String sku);

    @PostMapping("/reserve")
    ResponseEntity<String> reserveStock(@RequestParam("sku") String sku, @RequestParam("quantity") int quantity);

}
