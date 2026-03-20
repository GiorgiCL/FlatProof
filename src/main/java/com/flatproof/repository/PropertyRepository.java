package com.flatproof.repository;

import com.flatproof.entity.Property;
import com.flatproof.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByCreatedBy(User user);
}
