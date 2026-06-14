package com.dev.featureflag.controller;

import com.dev.featureflag.dto.EvaluationResponse;
import com.dev.featureflag.dto.FlagRequest;
import com.dev.featureflag.dto.FlagResponse;
import com.dev.featureflag.dto.OverrideRequest;
import com.dev.featureflag.service.EvaluationService;
import com.dev.featureflag.service.FlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flags")
public class FlagController {

    private final FlagService flagService;
    private final EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<List<FlagResponse>> getAllFlags() {
        return ResponseEntity.ok(flagService.getAllFlags());
    }

    @GetMapping("/evaluate")
    public ResponseEntity<EvaluationResponse> evaluateFlag(
            @RequestParam String flagName,
            @RequestParam(required = false) String userId) {
        return ResponseEntity.ok(evaluationService.evaluate(flagName, userId));
    }

    @PostMapping
    public ResponseEntity<FlagResponse> createFlag(@RequestBody FlagRequest request) {
        return ResponseEntity.ok(flagService.createFlag(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FlagResponse> updateFlag(@PathVariable Long id, @RequestBody FlagRequest request) {
        return ResponseEntity.ok(flagService.updateFlag(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlag(@PathVariable Long id) {
        flagService.deleteFlag(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/overrides")
    public ResponseEntity<Void> addOverride(@PathVariable Long id, @RequestBody OverrideRequest overrideRequest) {
        flagService.addOverride(id, overrideRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/overrides/{userId}")
    public ResponseEntity<Void> removeOverride(@PathVariable Long id, @PathVariable String userId) {
        flagService.removeOverride(id, userId);
        return ResponseEntity.noContent().build();
    }
}
