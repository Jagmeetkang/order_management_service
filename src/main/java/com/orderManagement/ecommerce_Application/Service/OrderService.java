package com.orderManagement.ecommerce_Application.Service;

import com.orderManagement.ecommerce_Application.Model.Orders;
import com.orderManagement.ecommerce_Application.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    public Orders placeOrder(Orders orders){
        return orderRepository.save(orders);
    }

    public List<Orders> getAllOrders(){
        return orderRepository.findAll();
    }

    public Orders getOrderById(Long id){
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteOrder(Long id){
        orderRepository.deleteById(id);
    }


}
