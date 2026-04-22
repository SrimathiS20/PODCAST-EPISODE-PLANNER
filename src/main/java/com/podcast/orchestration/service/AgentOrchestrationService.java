package com.podcast.orchestration.service;

import com.podcast.orchestration.agent.Agent;
import com.podcast.orchestration.agent.impl.*;
import com.podcast.orchestration.dto.ContextDTO;
import com.podcast.orchestration.dto.ExecutionResult;
import com.podcast.orchestration.entity.ExecutionHistory;
import com.podcast.orchestration.enums.AgentType;
import com.podcast.orchestration.repository.ExecutionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Orchestrator / Service Layer - THE EXECUTOR (Hands of the system)
 * 
 * Responsibilities:
 * 1. Receives decision from Context Agent
 * 2. Executes the decided agent
 * 3. Tracks execution history
 * 4. Returns updated context
 * 
 * This maintains SEPARATION OF CONCERNS:
 * - Context Agent = Decision Making
 * - Orchestrator = Execution
 */
@Service
public class AgentOrchestrationService {
    
    @Autowired
    private BackendAgent backendAgent;
    
    @Autowired
    private QuestionBankAgent questionBankAgent;
    
    @Autowired
    private ScriptAgent scriptAgent;
    
    @Autowired
    private ContextAgent contextAgent;
    
    @Autowired
    private ExecutionHistoryRepository executionHistoryRepository;
    
    // Map of agent types to agent implementations
    private Map<AgentType, Agent> agentMap;
    
    /**
     * Initialize agent map (done once at startup)
     */
    @Autowired
    public void initializeAgentMap() {
        agentMap = new HashMap<>();
        agentMap.put(AgentType.BACKEND, backendAgent);
        agentMap.put(AgentType.QUESTION_BANK, questionBankAgent);
        agentMap.put(AgentType.SCRIPT, scriptAgent);
    }
    
    /**
     * Main orchestration workflow
     * 
     * Flow:
     * 1. Context Agent decides
     * 2. Orchestrator executes
     * 3. Track history
     * 4. Return result
     */
    public ExecutionResult orchestrate(ContextDTO context, String userAction) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 ORCHESTRATION CYCLE STARTED");
        System.out.println("=".repeat(60));
        
        // STEP 1: Context Agent DECIDES
        ExecutionResult result = contextAgent.buildAndDecideNextAgent(context, userAction);
        
        // STEP 2: If terminal, return
        if (result.isTerminal()) {
            System.out.println("✅ Workflow completed - no more agents to execute");
            return result;
        }
        
        // STEP 3: Orchestrator EXECUTES the decided agent
        AgentType nextAgent = result.getNextAgent();
        ContextDTO updatedContext = executeAgent(nextAgent, result.getContextDTO());
        
        // STEP 4: Track execution history
        trackExecution(context.getEpisodeId(), nextAgent, result.getExecutionId(), userAction);
        
        // Update result with executed context
        result.setContextDTO(updatedContext);
        
        System.out.println("=".repeat(60));
        System.out.println("✅ ORCHESTRATION CYCLE COMPLETE");
        System.out.println("=".repeat(60) + "\n");
        
        return result;
    }
    
    /**
     * Execute a specific agent
     * THIS IS WHERE EXECUTION HAPPENS
     */
    private ContextDTO executeAgent(AgentType agentType, ContextDTO context) {
        System.out.println("\n► Executing Agent: " + agentType.getDescription());
        
        Agent agent = agentMap.get(agentType);
        if (agent == null) {
            throw new IllegalArgumentException("Unknown agent type: " + agentType);
        }
        
        // Check if agent can execute
        if (!agent.canExecute(context)) {
            System.out.println("❌ Agent cannot execute in current state");
            throw new IllegalStateException("Agent " + agentType + " cannot execute with current context");
        }
        
        // Execute the agent
        ContextDTO result = agent.execute(context);
        
        System.out.println("► Agent completed successfully\n");
        return result;
    }
    
    /**
     * Track execution in database
     */
    private void trackExecution(Long episodeId, AgentType agentType, String executionId, String userAction) {
        ExecutionHistory history = ExecutionHistory.builder()
            .episodeId(episodeId)
            .executedAgent(agentType)
            .executionId(executionId)
            .executedAt(LocalDateTime.now())
            .status("SUCCESS")
            .userAction(userAction)
            .build();
        
        // Set execution order
        Integer order = executionHistoryRepository.findByEpisodeIdOrderByExecutionOrderAsc(episodeId).size() + 1;
        history.setExecutionOrder(order);
        
        executionHistoryRepository.save(history);
        System.out.println("📝 Execution tracked: " + agentType + " (Order: " + order + ")");
    }
    
    /**
     * Continuous workflow - keep orchestrating until done
     */
    public ExecutionResult orchestrateContinuous(ContextDTO context, String userAction) {
        ExecutionResult result = orchestrate(context, userAction);
        
        // Keep orchestrating until terminal
        while (!result.isTerminal()) {
            result = orchestrate(result.getContextDTO(), null);
        }
        
        return result;
    }
}
