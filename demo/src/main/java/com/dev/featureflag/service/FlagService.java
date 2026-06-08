package com.dev.featureflag.service;

import com.dev.featureflag.dto.FlagRequest;
import com.dev.featureflag.entity.Flag;
import com.dev.featureflag.repository.FlagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlagService {

    private final FlagRepository flagRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<Boolean> addNewFlag(FlagRequest flagRequest) {
        Flag flag = new Flag();
        modelMapper.map(flagRequest, flag);
        flagRepository.save(flag);
        return ResponseEntity.ok(true);
    }
}
