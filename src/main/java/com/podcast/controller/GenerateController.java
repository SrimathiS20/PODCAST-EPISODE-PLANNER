package com.podcast.controller;

import com.podcast.dto.EpisodeRequest;
import com.podcast.dto.FinalResponse;
import com.podcast.service.GenerateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GenerateController {

    private final GenerateService generateService;

    public GenerateController(GenerateService generateService) {
        this.generateService = generateService;
    }

    @PostMapping("/generate")
    public ResponseEntity<FinalResponse> generateEpisode(@Valid @RequestBody EpisodeRequest request) {
        FinalResponse response = generateService.generateEpisode(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generate")
    public ResponseEntity<FinalResponse> getLatestResponse() {
        FinalResponse response = generateService.getLatestResponse();
        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generate/all")
    public ResponseEntity<List<FinalResponse>> getAllResponses() {
        return ResponseEntity.ok(generateService.getAllResponses());
    }
}
