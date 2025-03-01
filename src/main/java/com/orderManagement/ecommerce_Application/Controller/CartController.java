package com.orderManagement.ecommerce_Application.Controller;

import com.orderManagement.ecommerce_Application.Model.OrderItem;
import com.orderManagement.ecommerce_Application.Model.Orders;
import com.orderManagement.ecommerce_Application.Service.CartService;
import com.orderManagement.ecommerce_Application.Service.OrderService;
import com.orderManagement.ecommerce_Application.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    @Autowired
    public  final CartService cartService;

    @PostMapping("/addProduct")
    public String addToCart(@RequestHeader("X-User-Id") String userId, @RequestBody List<OrderItem> orderItems){
        return cartService.addToCart(userId, orderItems);
    }

    @PostMapping("/checkout")
    public String checkout(@RequestHeader("X-User-Id") String userId, @RequestParam Long id, @RequestHeader("Authorization") String token ){
        return cartService.checkout(userId,id,token);
    }

    @GetMapping("/view")
    public Orders viewCart(@RequestHeader("X-User-Id") String userId){
        return cartService.viewCart(userId);
    }

    @PostMapping("/empty")
    public String emptyCart(@RequestHeader("X-User-Id") String userId){
        return cartService.emptyCart(userId);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam Long id, @RequestHeader("Authorization") String token){
        return cartService.getDetails(id,token);
    }



}
