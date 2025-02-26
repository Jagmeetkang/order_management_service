package com.orderManagement.ecommerce_Application.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.orderManagement.ecommerce_Application.Event.OrderEventPublisher;
import com.orderManagement.ecommerce_Application.Model.OrderItem;
import com.orderManagement.ecommerce_Application.Model.Orders;
import com.orderManagement.ecommerce_Application.Repository.OrderRepository;
import com.orderManagement.ecommerce_Application.dto.OrderDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    public String addToCart(String userId, List<OrderItem> items){
        Orders order = new Orders();
        if(orderRepository.findOrdersByUserIdAndStatus(userId,"Draft") != null){
            order = orderRepository.findOrdersByUserIdAndStatus(userId,"Draft");
            List<OrderItem> existingItems = order.getItems();
            for(OrderItem newitem:items) {
                boolean isFound=false;
                for (OrderItem item : existingItems) {
                    if (item.getItemSku().equals(newitem.getItemSku())) {
                        item.setQuantity(item.getQuantity() + newitem.getQuantity());
                        isFound=true;
                        break;
                    }
                }
                if(!isFound){
                    existingItems.add(newitem);
                }
            }
            order.setItems(existingItems);
        }else{
            order.setUserId(userId);
            order.setOrderNumber("1002");
            order.setStatus("Draft");
            order.setItems(items);
        }
        orderRepository.save(order);
        return "Cart updated ";
    }

    public Orders viewCart(String userId){
        return orderRepository.findOrdersByUserIdAndStatus(userId,"Draft");
    }

    public String emptyCart(String userId){
        Orders order = orderRepository.findOrdersByUserIdAndStatus(userId,"Draft");
        order.getItems().clear();
        orderRepository.save(order);
        return "Cart cleared";
    }

    public String checkout(String userId){
        Orders order = orderRepository.findOrdersByUserIdAndStatus(userId,"Draft");
        if(order == null){
            return "No items in the cart";
        }else{
            order.setStatus("Placed");
            orderRepository.save(order);
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderId(order.getOrderNumber());
            orderDetails.setEmail("yadhurk1992@gmail.com");
            orderDetails.setShippingAddress("12232 Avenue rd, Toronto");
            try {
                orderEventPublisher.publishOrderEvent(orderDetails);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return "Order placed with order number : "+order.getOrderNumber();
        }

    }

}
