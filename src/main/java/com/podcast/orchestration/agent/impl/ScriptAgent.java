package com.podcast.orchestration.agent.impl;

import com.podcast.orchestration.agent.Agent;
import com.podcast.orchestration.dto.ContextDTO;
import org.springframework.stereotype.Component;

/**
 * Script Agent - Generates the full podcast script
 * Uses questions and backend context to create the script
 */
@Component
public class ScriptAgent implements Agent {
    
    @Override
    public ContextDTO execute(ContextDTO context) {
        System.out.println("📝 Script Agent executing...");
        
        // Questions should ideally be generated first
        if (!context.isQuestionsGenerated()) {
            System.out.println("⚠️  Warning: Questions not generated yet, but proceeding with script generation");
        }
        
        String scriptPrompt = buildScriptPrompt(context);
        context.setScriptPrompt(scriptPrompt);
        context.setScriptGenerated(true);
        
        System.out.println("✅ Script Agent: Generated podcast script");
        return context;
    }
    
    @Override
    public String getAgentName() {
        return "ScriptAgent";
    }
    
    @Override
    public boolean canExecute(ContextDTO context) {
        // Can execute after backend, ideally after questions too
        return context.isBackendDataProcessed() && !context.isScriptGenerated();
    }
    
    private String buildScriptPrompt(ContextDTO context) {
        String questionContext = context.isQuestionsGenerated() 
            ? "Using the generated interview questions as reference" 
            : "Create questions and flow within the script";
            
        return String.format(
            "Generate a complete podcast script for '%s' episode. " +
            "Host: '%s', Guest: '%s' (%s). " +
            "%s. " +
            "Create engaging introduction, interview flow, and conclusion. " +
            "Include: opening remarks, guest introduction, interview Q&A flow, key insights, and closing.",
            context.getEpisodeTitle(),
            context.getHostName(),
            context.getGuestName(),
            context.getGuestExpertise(),
            questionContext
        );
    }
}
