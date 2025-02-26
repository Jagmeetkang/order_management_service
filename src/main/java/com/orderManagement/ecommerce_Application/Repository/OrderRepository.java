package com.orderManagement.ecommerce_Application.Repository;

import com.orderManagement.ecommerce_Application.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders,Long> {
    Orders findOrdersByUserIdAndStatus(String userIdAfter, String status);
}
