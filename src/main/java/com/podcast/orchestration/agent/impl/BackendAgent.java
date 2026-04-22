package com.podcast.orchestration.agent.impl;

import com.podcast.orchestration.agent.Agent;
import com.podcast.orchestration.dto.ContextDTO;
import org.springframework.stereotype.Component;

/**
 * Backend Agent - Processes data and prepares backend information
 * Generates backend context and prompts
 */
@Component
public class BackendAgent implements Agent {
    
    @Override
    public ContextDTO execute(ContextDTO context) {
        System.out.println("🔧 Backend Agent executing...");
        
        // Build backend prompt based on episode and guest info
        String backendPrompt = buildBackendPrompt(context);
        context.setBackendPrompt(backendPrompt);
        context.setBackendDataProcessed(true);
        
        System.out.println("✅ Backend Agent: Generated backend context");
        return context;
    }
    
    @Override
    public String getAgentName() {
        return "BackendAgent";
    }
    
    @Override
    public boolean canExecute(ContextDTO context) {
        // Backend can always execute at the start
        return !context.isBackendDataProcessed();
    }
    
    private String buildBackendPrompt(ContextDTO context) {
        return String.format(
            "Process episode '%s' with guest '%s' (Expertise: %s). " +
            "Prepare backend data including guest background: %s",
            context.getEpisodeTitle(),
            context.getGuestName(),
            context.getGuestExpertise(),
            context.getGuestBackground()
        );
    }
}
