package com.orderManagement.ecommerce_Application.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {
    private static final String TOPIC="order-events";
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void publishOrderEvent(String orderEvent){
        kafkaTemplate.send(TOPIC,orderEvent);
    }
}
