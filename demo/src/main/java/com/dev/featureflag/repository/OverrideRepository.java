package com.dev.featureflag.repository;

import com.dev.featureflag.entity.Override;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OverrideRepository extends JpaRepository<Override, Long> {
    Optional<Override> findByFlagIdAndUserId(Long flagId, String userId);
    void deleteByFlagIdAndUserId(Long flagId, String userId);
}
