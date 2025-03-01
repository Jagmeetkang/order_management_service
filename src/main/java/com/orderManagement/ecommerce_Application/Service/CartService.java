package com.orderManagement.ecommerce_Application.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.orderManagement.ecommerce_Application.Event.OrderEventPublisher;
import com.orderManagement.ecommerce_Application.Model.OrderItem;
import com.orderManagement.ecommerce_Application.Model.Orders;
import com.orderManagement.ecommerce_Application.Repository.OrderRepository;
import com.orderManagement.ecommerce_Application.client.InventoryServiceClient;
import com.orderManagement.ecommerce_Application.client.UserManagementClient;
import com.orderManagement.ecommerce_Application.dto.OrderDetails;
import com.orderManagement.ecommerce_Application.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Autowired
    private InventoryServiceClient inventoryServiceClient;

    @Autowired
    private UserManagementClient userManagementClient;

    public String addToCart(String userId, List<OrderItem> items){
        StringBuilder stringBuilder = new StringBuilder();
        Orders order = new Orders();
        if(orderRepository.findOrdersByUserIdAndStatus(userId,"Draft") != null){
            order = orderRepository.findOrdersByUserIdAndStatus(userId,"Draft");
            List<OrderItem> existingItems = order.getItems();
            for(OrderItem newitem:items) {
                int stock = getQuantity(newitem.getItemSku());
                boolean isFound=false;
                for (OrderItem item : existingItems) {
                    if (item.getItemSku().equals(newitem.getItemSku())) {
                        if(stock >= item.getQuantity() + newitem.getQuantity()){
                            item.setQuantity(item.getQuantity() + newitem.getQuantity());
                        }else{
//                            item.setQuantity(item.getQuantity()+stock);
                            stringBuilder.append("Item : "+ item.getItemName()+"(" +
                                    item.getItemSku() + ") is out of stock." +
                                    " Only "+stock+" units available" );
                        }

                        isFound=true;
                        break;
                    }
                }
                if(!isFound){
                    if(stock>newitem.getQuantity()){
                        existingItems.add(newitem);
                    }else{
                        stringBuilder.append(" Item "+newitem.getItemName()+"(" +
                                newitem.getItemSku()+ ") is out of stock. " +
                                "only "+stock+ " units available");
                    }

                }
            }
            order.setItems(existingItems);
        }else{
            boolean isStockAvailable=true;
            for(OrderItem item:items){
                int checkStock =  getQuantity(item.getItemSku());
                if(checkStock < item.getQuantity()){
                    stringBuilder.append(" Cannot add product "+item.getItemName()+" " +
                            "is out of stock. "+ "only "+checkStock+ " available");
                    isStockAvailable=false;
                }
            }
            if(isStockAvailable){
                order.setUserId(userId);
                order.setOrderNumber("1002");
                order.setStatus("Draft");
                order.setItems(items);
                stringBuilder.append("All products added to cart");
            }
        }
        orderRepository.save(order);
        return "Cart Status : " + stringBuilder;
    }

    public Orders viewCart(String userId){
        return orderRepository.findOrdersByUserIdAndStatus(userId,"Draft");
    }

    public int getQuantity(String sku){
        return inventoryServiceClient.getStock(sku).getBody();
    }

    public String reserveStock(String sku,int quantity){
        return inventoryServiceClient.reserveStock(sku,quantity).getBody();
    }

    public String emptyCart(String userId){
        Orders order = orderRepository.findOrdersByUserIdAndStatus(userId,"Draft");
        order.getItems().clear();
        orderRepository.save(order);
        return "Cart cleared";
    }

    public ResponseEntity<User> getDetails(Long id, String token){
        return userManagementClient.getUserProfile(id,token);
    }

    public String checkout(String userId, Long id, String token){
        StringBuilder stringBuilder=new StringBuilder();
        Orders order = orderRepository.findOrdersByUserIdAndStatus(userId,"Draft");
        if(order == null){
            return "No items in the cart";
        }else{
            boolean isAvailable=true;
            for(OrderItem item:order.getItems()){
                if(getQuantity(item.getItemSku()) < item.getQuantity()){
                    isAvailable=false;
                    stringBuilder.append("item "+ item.getItemName() + "("+item.getItemSku()+") is out of stock" );
                }
            }
            //Todo - optimize this logic by added reserve all endpoint
            if (isAvailable){
                for(OrderItem item:order.getItems()){
                    reserveStock(item.getItemSku(),item.getQuantity());
                }
                order.setStatus("Placed");
                orderRepository.save(order);
                User user = getDetails(id,token).getBody();
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrderId(order.getOrderNumber());
                orderDetails.setEmail(user.getEmail());
                orderDetails.setShippingAddress(user.getAddress());
                try {
                    orderEventPublisher.publishOrderEvent(orderDetails);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return "Order placed with order number : "+order.getOrderNumber();
            }else{
                return "Oops!! Order couldn't be placed" + stringBuilder;
            }

        }

    }

}
