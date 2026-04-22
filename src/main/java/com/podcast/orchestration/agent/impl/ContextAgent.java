package com.podcast.orchestration.agent.impl;

import com.podcast.orchestration.dto.ContextDTO;
import com.podcast.orchestration.dto.ExecutionResult;
import com.podcast.orchestration.enums.AgentType;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Context Agent - THE DECISION MAKER (Brain of the system)
 * 
 * Responsibilities:
 * 1. Builds and maintains ContextDTO
 * 2. Tracks execution history
 * 3. Decides which agent executes next based on:
 *    - Execution history (what already ran)
 *    - User interaction (what user clicked)
 * 
 * Does NOT execute agents - that's Orchestrator's job
 */
@Component
public class ContextAgent {
    
    /**
     * Main decision-making logic
     * 
     * @param context Current context
     * @param userAction What user clicked (e.g., "GENERATE_QUESTIONS", "GENERATE_SCRIPT")
     * @return ExecutionResult with decision + updated context
     */
    public ExecutionResult buildAndDecideNextAgent(ContextDTO context, String userAction) {
        System.out.println("🧠 Context Agent: Analyzing execution flow...");
        
        ExecutionResult result = ExecutionResult.builder()
            .executionId(UUID.randomUUID().toString())
            .timestamp(LocalDateTime.now())
            .status("SUCCESS")
            .build();
        
        // DECISION LOGIC based on execution history + user interaction
        AgentType nextAgent = determineNextAgent(context, userAction);
        String reason = buildDecisionReason(context, userAction, nextAgent);
        
        result.setNextAgent(nextAgent);
        result.setContextDTO(context);
        result.setReason(reason);
        
        // Check if we're done
        if (nextAgent == null) {
            result.setTerminal(true);
            result.setReason("All agents executed. Workflow complete.");
            System.out.println("🛑 Context Agent: No more agents to execute");
        } else {
            result.setTerminal(false);
            System.out.println("✅ Context Agent: Decided next agent = " + nextAgent);
        }
        
        return result;
    }
    
    /**
     * Determine which agent should execute next
     * 
     * Decision Tree:
     * 1. If user clicked "GENERATE_SCRIPT" → ScriptAgent (override)
     * 2. If user clicked "GENERATE_QUESTIONS" → QuestionBankAgent (override)
     * 3. Otherwise, default flow: Backend → Questions → Script
     */
    private AgentType determineNextAgent(ContextDTO context, String userAction) {
        
        // Check user override actions
        if ("GENERATE_SCRIPT".equals(userAction)) {
            // User wants script immediately
            if (!context.isBackendDataProcessed()) {
                // Need backend first
                return AgentType.BACKEND;
            }
            return AgentType.SCRIPT;
        }
        
        if ("GENERATE_QUESTIONS".equals(userAction)) {
            // User wants questions
            if (!context.isBackendDataProcessed()) {
                // Need backend first
                return AgentType.BACKEND;
            }
            return AgentType.QUESTION_BANK;
        }
        
        // Default flow based on execution history
        if (!context.isBackendDataProcessed()) {
            return AgentType.BACKEND;
        }
        
        if (!context.isQuestionsGenerated()) {
            return AgentType.QUESTION_BANK;
        }
        
        if (!context.isScriptGenerated()) {
            return AgentType.SCRIPT;
        }
        
        // All agents executed
        return null;
    }
    
    private String buildDecisionReason(ContextDTO context, String userAction, AgentType nextAgent) {
        if (nextAgent == null) {
            return "Workflow complete. All agents have executed.";
        }
        
        StringBuilder reason = new StringBuilder();
        reason.append("Execution state: [");
        reason.append("Backend=").append(context.isBackendDataProcessed() ? "✅" : "❌").append(", ");
        reason.append("Questions=").append(context.isQuestionsGenerated() ? "✅" : "❌").append(", ");
        reason.append("Script=").append(context.isScriptGenerated() ? "✅" : "❌").append("]");
        
        if (userAction != null && !userAction.isEmpty()) {
            reason.append(" | User action: ").append(userAction);
        }
        
        return reason.toString();
    }
}
