package com.orderManagement.ecommerce_Application.Event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderManagement.ecommerce_Application.dto.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {
    private static final String TOPIC="shipping-details";

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void publishOrderEvent(OrderDetails orderDetails) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String orderDetailsJson = objectMapper.writeValueAsString(orderDetails);
        kafkaTemplate.send("shipping-details",orderDetailsJson);
    }
}
