package com.dev.featureflag.controller;

import com.dev.featureflag.dto.FlagRequest;
import com.dev.featureflag.service.FlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FlagController {

    private final FlagService flagService;

    @PostMapping("/flags")
    public ResponseEntity<Boolean> flag(@RequestBody FlagRequest flagRequest) {
        return flagService.addNewFlag(flagRequest);
    }
}
