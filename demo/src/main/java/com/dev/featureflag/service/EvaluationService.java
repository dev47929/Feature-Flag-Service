package com.dev.featureflag.service;

import com.dev.featureflag.dto.EvaluationResponse;
import com.dev.featureflag.entity.Flag;
import com.dev.featureflag.entity.Override;
import com.dev.featureflag.repository.FlagRepository;
import com.dev.featureflag.repository.OverrideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final FlagRepository flagRepository;
    private final OverrideRepository overrideRepository;

    public EvaluationResponse evaluate(String flagName, String userId) {
        Flag flag = flagRepository.findAll().stream()
                .filter(f -> f.getName().equals(flagName))
                .findFirst()
                .orElse(null);

        if (flag == null) {
            return EvaluationResponse.builder()
                    .flagName(flagName)
                    .enabled(false)
                    .userId(userId)
                    .reason("FLAG_NOT_FOUND")
                    .build();
        }

        List<Override> overrides = overrideRepository.findAll().stream()
                .filter(o -> o.getFlagId().equals(flag.getId()) && o.getUserId().equals(userId))
                .toList();

        if (!overrides.isEmpty()) {
            return EvaluationResponse.builder()
                    .flagName(flagName)
                    .enabled(overrides.get(0).isEnabled())
                    .userId(userId)
                    .reason("OVERRIDE")
                    .build();
        }

        boolean enabled = flag.isEnabled() && (flag.getRolloutPercentage() == null || flag.getRolloutPercentage() >= 100);
        return EvaluationResponse.builder()
                .flagName(flagName)
                .enabled(enabled)
                .userId(userId)
                .reason("ROLLOUT")
                .build();
    }
}
