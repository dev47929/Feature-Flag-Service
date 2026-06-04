package com.example.flagFeature.Repository;

import com.example.flagFeature.Entity.FlagFeaturesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlagFeaturesRepo extends JpaRepository<FlagFeaturesEntity,Long> {
}
