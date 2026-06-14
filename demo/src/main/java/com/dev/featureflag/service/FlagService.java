package com.dev.featureflag.service;

import com.dev.featureflag.dto.FlagRequest;
import com.dev.featureflag.dto.FlagResponse;
import com.dev.featureflag.dto.OverrideRequest;
import com.dev.featureflag.entity.Flag;
import com.dev.featureflag.entity.Override;
import com.dev.featureflag.repository.FlagRepository;
import com.dev.featureflag.repository.OverrideRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlagService {

    private final FlagRepository flagRepository;
    private final OverrideRepository overrideRepository;
    private final ModelMapper modelMapper;

    public List<FlagResponse> getAllFlags() {
        return flagRepository.findAll()
                .stream()
                .map(flag -> modelMapper.map(flag, FlagResponse.class))
                .toList();
    }

    public FlagResponse createFlag(FlagRequest request) {
        Flag flag = new Flag();
        modelMapper.map(request, flag);
        flag.setCreatedAt(LocalDateTime.now());
        flagRepository.save(flag);
        return modelMapper.map(flag, FlagResponse.class);
    }

    public FlagResponse updateFlag(Long id, FlagRequest request) {
        Flag flag = flagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Flag not found: " + id));
        if (request.getName() != null) flag.setName(request.getName());
        if (request.getDescription() != null) flag.setDescription(request.getDescription());
        flag.setEnabled(request.isEnabled());
        if (request.getRolloutPercentage() != null) flag.setRolloutPercentage(request.getRolloutPercentage());
        if (request.getEnvironment() != null) flag.setEnvironment(request.getEnvironment());
        flagRepository.save(flag);
        return modelMapper.map(flag, FlagResponse.class);
    }

    public void deleteFlag(Long id) {
        if (!flagRepository.existsById(id)) {
            throw new EntityNotFoundException("Flag not found: " + id);
        }
        flagRepository.deleteById(id);
    }

    public void addOverride(Long flagId, OverrideRequest request) {
        if (!flagRepository.existsById(flagId)) {
            throw new EntityNotFoundException("Flag not found: " + flagId);
        }

        Override override = new Override();
        override.setFlagId(flagId);
        override.setUserId(request.getUserId());
        override.setEnabled(request.isEnabled());

        overrideRepository.save(override);
    }

    public void removeOverride(Long flagId, String userId) {
        Override override = overrideRepository.findByFlagIdAndUserId(flagId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Override not found for flag " + flagId + " and user " + userId));
        overrideRepository.delete(override);
    }
}
