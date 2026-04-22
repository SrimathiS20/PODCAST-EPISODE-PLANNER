package com.podcast.orchestration.controller;

import com.podcast.orchestration.dto.ContextDTO;
import com.podcast.orchestration.dto.ExecutionResult;
import com.podcast.orchestration.service.AgentOrchestrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller - Frontend interaction point
 * Receives user requests and triggers orchestration
 */
@RestController
@RequestMapping("/api/orchestration")
@CrossOrigin(origins = "*")
public class OrchestrationController {
    
    @Autowired
    private AgentOrchestrationService orchestrationService;
    
    /**
     * Initialize a new episode workflow
     * POST /api/orchestration/start
     */
    @PostMapping("/start")
    public ResponseEntity<?> startWorkflow(@RequestBody ContextDTO contextDTO) {
        System.out.println("\n📢 Frontend Request: Starting workflow for episode: " + contextDTO.getEpisodeTitle());
        
        // Reset all flags
        contextDTO.setBackendDataProcessed(false);
        contextDTO.setQuestionsGenerated(false);
        contextDTO.setScriptGenerated(false);
        
        // Start orchestration (one step at a time)
        ExecutionResult result = orchestrationService.orchestrate(contextDTO, null);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * User clicks "Generate Questions"
     * POST /api/orchestration/generate-questions
     */
    @PostMapping("/generate-questions")
    public ResponseEntity<?> generateQuestions(@RequestBody ContextDTO contextDTO) {
        System.out.println("\n📢 Frontend Request: User clicked 'Generate Questions'");
        
        // This tells Context Agent: "User wants questions"
        ExecutionResult result = orchestrationService.orchestrate(contextDTO, "GENERATE_QUESTIONS");
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * User clicks "Generate Script"
     * POST /api/orchestration/generate-script
     */
    @PostMapping("/generate-script")
    public ResponseEntity<?> generateScript(@RequestBody ContextDTO contextDTO) {
        System.out.println("\n📢 Frontend Request: User clicked 'Generate Script'");
        
        // This tells Context Agent: "User wants script"
        ExecutionResult result = orchestrationService.orchestrate(contextDTO, "GENERATE_SCRIPT");
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Continuous workflow - execute all remaining agents
     * POST /api/orchestration/run-all
     */
    @PostMapping("/run-all")
    public ResponseEntity<?> runAllAgents(@RequestBody ContextDTO contextDTO) {
        System.out.println("\n📢 Frontend Request: Run all agents continuously");
        
        // Run all agents until workflow is complete
        ExecutionResult result = orchestrationService.orchestrateContinuous(contextDTO, null);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("Orchestration service is running");
    }
}
