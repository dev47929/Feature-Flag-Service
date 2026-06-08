package com.dev.featureflag.repository;

import com.dev.featureflag.entity.Override;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OverrideRepository extends JpaRepository<Override, Long> {
}
