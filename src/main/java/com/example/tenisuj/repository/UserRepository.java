package com.example.tenisuj.repository;

import com.example.tenisuj.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByUsernameContainingIgnoreCase(String searchText);

    User findUserByPlayerId(String playerId);
}
