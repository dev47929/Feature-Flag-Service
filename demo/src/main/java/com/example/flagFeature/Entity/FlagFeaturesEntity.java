package com.example.flagFeature.Entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlagFeaturesEntity {
    private Long id;
    private String name;
    private String description;
    private boolean enabled;
    private Integer rolloutPercentage;
    private String environment;
    private LocalDateTime createdAt;
}
