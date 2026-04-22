package com.podcast.orchestration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application
 * Agent Orchestration System for Podcast Production
 */
@SpringBootApplication
public class AgentOrchestrationApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AgentOrchestrationApplication.class, args);
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🎙️  PODCAST AGENT ORCHESTRATION SYSTEM STARTED");
        System.out.println("=".repeat(70));
        System.out.println("Available Endpoints:");
        System.out.println("  POST /api/orchestration/start           - Start new workflow");
        System.out.println("  POST /api/orchestration/generate-questions - Generate questions");
        System.out.println("  POST /api/orchestration/generate-script    - Generate script");
        System.out.println("  POST /api/orchestration/run-all            - Run all agents");
        System.out.println("  GET  /api/orchestration/health             - Health check");
        System.out.println("=".repeat(70) + "\n");
    }
}
