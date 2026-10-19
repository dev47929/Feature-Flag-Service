package com.dev.featureflag.controller;

import com.dev.featureflag.dto.EvaluationResponse;
import com.dev.featureflag.dto.FlagResponse;
import com.dev.featureflag.service.EvaluationService;
import com.dev.featureflag.service.FlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flags")
public class UserController {

    private final FlagService flagService;
    private final EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<List<FlagResponse>> getAllFlags() {
        return ResponseEntity.ok(flagService.getAllFlags());
    }

    @GetMapping("/evaluate")
    public ResponseEntity<EvaluationResponse> evaluateFlag(@RequestParam String flagName, @RequestParam(required = false) String userId) {
        return ResponseEntity.ok(evaluationService.evaluate(flagName, userId));
    }
}
