package com.dev.featureflag.controller;

import com.dev.featureflag.dto.FlagRequest;
import com.dev.featureflag.service.FlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flags")
public class FlagController {

    private final FlagService flagService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> flag(@RequestBody FlagRequest flagRequest) {
        return flagService.addNewFlag(flagRequest);
    }
}
