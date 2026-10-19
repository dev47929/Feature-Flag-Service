package com.dev.featureflag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlagResponse {
    private Long id;
    private String name;
    private String description;
    private boolean enabled;
    private Integer rolloutPercentage;
    private String environment;
    private LocalDateTime createdAt;
}
