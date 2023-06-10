package com.shopping.electronic.store.repository;

import com.shopping.electronic.store.model.Order;
import com.shopping.electronic.store.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);
}
