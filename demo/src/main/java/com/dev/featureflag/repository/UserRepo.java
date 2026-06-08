package com.dev.featureflag.repository;

import com.dev.featureflag.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByUsername(String username);
    boolean existsByUsername(String username);
}
