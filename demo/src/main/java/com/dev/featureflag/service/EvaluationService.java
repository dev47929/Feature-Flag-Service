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

}
