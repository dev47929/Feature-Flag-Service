package com.example.flagFeature.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlagFeaturesDto {
    private String name;
    private String description;
    private boolean enabled;
    private Integer rolloutPercentage;
    private String environment;
}
