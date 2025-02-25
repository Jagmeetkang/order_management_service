package com.orderManagement.ecommerce_Application.Controller;

import com.orderManagement.ecommerce_Application.Model.Order;
import com.orderManagement.ecommerce_Application.Repository.Service.OrderService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Data
@RestController
@RequestMapping("/orders")
@Autowired
public class Controller {
    public  OrderService orderService;

    @PostMapping
    public Order placeOrder(@RequestBody Order order){
        return orderService.placeOrder(order);
    }
    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
    }

}
