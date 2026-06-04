package com.example.flagFeature.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverrideFlagsDto {
    private Long id;
    private Long flagId;
    private String userId;
    private boolean enabled;
}
