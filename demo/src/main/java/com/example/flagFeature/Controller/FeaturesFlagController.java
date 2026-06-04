package com.example.flagFeature.Controller;

import com.example.flagFeature.Dto.FlagFeaturesDto;
import com.example.flagFeature.Service.FlagService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FeaturesFlagController {

    private final FlagService flagService;

    @PostMapping("/flags")
    public ResponseEntity<boolean[]> flag(@RequestBody FlagFeaturesDto flagFeaturesDto){
        return
    }
}
