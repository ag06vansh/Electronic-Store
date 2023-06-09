package com.shopping.electronic.store.repository;

import com.shopping.electronic.store.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    List<User> findByNameContaining(String keyword);
}
