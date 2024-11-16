package com.mmintegrations.auth_gateway.repository;

import com.mmintegrations.auth_gateway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM users_roles ur WHERE ur.user_id = ?1 AND ur.role_id = ?2) THEN 1 ELSE 0 END", nativeQuery = true)
    Integer hasRole(UUID userId, int roleId);

}
