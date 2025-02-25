package com.orderManagement.ecommerce_Application.Controller;

import com.orderManagement.ecommerce_Application.Model.Orders;
import com.orderManagement.ecommerce_Application.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")

public class Controller {
    public  final OrderService orderService;
    @Autowired
    public Controller(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/CreateOrder")
    public Orders placeOrder(@RequestBody Orders orders){
        return orderService.placeOrder(orders);
    }
    @GetMapping
    public List<Orders> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Orders getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
    }

}
