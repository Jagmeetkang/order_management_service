package com.orderManagement.ecommerce_Application.client;

import com.orderManagement.ecommerce_Application.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-management-service",url = "http://localhost:3000/users")
public interface UserManagementClient {

    @GetMapping("/{userId}")
    ResponseEntity<User> getUserProfile(@PathVariable Long userId, @RequestHeader("Authorization") String token );
}
