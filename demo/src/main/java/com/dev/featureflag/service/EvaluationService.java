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
        Flag flag = flagRepository.findByName(flagName);
        if (flag == null) {
            return EvaluationResponse.builder()
                    .flagName(flagName)
                    .enabled(false)
                    .userId(userId)
                    .reason("Flag not found")
                    .build();
        }

        if (userId != null) {
            Override override = overrideRepository.findByFlagIdAndUserId(flag.getId(), userId).orElse(null);
            if (override != null) {
                return EvaluationResponse.builder()
                        .flagName(flagName)
                        .enabled(override.isEnabled())
                        .userId(userId)
                        .reason("Override")
                        .build();
            }
        }

        if (!flag.isEnabled()) {
            return EvaluationResponse.builder()
                    .flagName(flagName)
                    .enabled(false)
                    .userId(userId)
                    .reason("Flag disabled")
                    .build();
        }

        Integer rollout = flag.getRolloutPercentage();
        if (rollout != null && rollout < 100) {
            if (userId != null) {
                int hash = Math.abs(userId.hashCode()) % 100;
                return EvaluationResponse.builder()
                        .flagName(flagName)
                        .enabled(hash < rollout)
                        .userId(userId)
                        .reason("Rollout percentage")
                        .build();
            }
            return EvaluationResponse.builder()
                    .flagName(flagName)
                    .enabled(false)
                    .userId(null)
                    .reason("Rollout percentage (no user)")
                    .build();
        }

        return EvaluationResponse.builder()
                .flagName(flagName)
                .enabled(true)
                .userId(userId)
                .reason("Flag enabled")
                .build();
    }
}
