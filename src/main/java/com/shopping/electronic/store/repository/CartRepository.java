package com.shopping.electronic.store.repository;

import com.shopping.electronic.store.model.Cart;
import com.shopping.electronic.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Cart findByUser(User user);
}
