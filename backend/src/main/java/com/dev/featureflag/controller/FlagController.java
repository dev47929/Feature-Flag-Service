package com.dev.featureflag.controller;

import com.dev.featureflag.dto.FlagRequest;
import com.dev.featureflag.dto.FlagResponse;
import com.dev.featureflag.dto.OverrideRequest;
import com.dev.featureflag.service.FlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flags")
public class FlagController {

    private final FlagService flagService;

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
