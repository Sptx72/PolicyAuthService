package com.mmintegrations.auth_gateway.repository;

import com.mmintegrations.auth_gateway.model.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Cacheable(value = "roles", key = "#name")
    @Query(value = "SELECT * FROM roles WHERE name = ?1", nativeQuery = true)
    Optional<Role> findByName(String name);
}
