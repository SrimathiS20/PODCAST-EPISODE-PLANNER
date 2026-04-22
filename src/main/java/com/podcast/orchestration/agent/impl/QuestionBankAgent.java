package com.podcast.orchestration.agent.impl;

import com.podcast.orchestration.agent.Agent;
import com.podcast.orchestration.dto.ContextDTO;
import org.springframework.stereotype.Component;

/**
 * Question Bank Agent - Generates questions based on guest expertise
 * Uses backend context to create targeted questions
 */
@Component
public class QuestionBankAgent implements Agent {
    
    @Override
    public ContextDTO execute(ContextDTO context) {
        System.out.println("❓ Question Bank Agent executing...");
        
        // Check if backend was processed first
        if (!context.isBackendDataProcessed()) {
            throw new IllegalStateException("Backend must execute before Question Bank");
        }
        
        String questionBankPrompt = buildQuestionPrompt(context);
        context.setQuestionBankPrompt(questionBankPrompt);
        context.setQuestionsGenerated(true);
        
        System.out.println("✅ Question Bank Agent: Generated interview questions");
        return context;
    }
    
    @Override
    public String getAgentName() {
        return "QuestionBankAgent";
    }
    
    @Override
    public boolean canExecute(ContextDTO context) {
        // Can execute only after backend is done
        return context.isBackendDataProcessed() && !context.isQuestionsGenerated();
    }
    
    private String buildQuestionPrompt(ContextDTO context) {
        return String.format(
            "Generate 10 insightful interview questions for '%s' who is a %s expert. " +
            "Episode: '%s'. Host: '%s'. " +
            "Create questions that explore %s expertise and insights.",
            context.getGuestName(),
            context.getGuestExpertise(),
            context.getEpisodeTitle(),
            context.getHostName(),
            context.getGuestExpertise()
        );
    }
}
