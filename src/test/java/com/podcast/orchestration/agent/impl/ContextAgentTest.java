package com.podcast.orchestration.agent.impl;

import com.podcast.orchestration.dto.ContextDTO;
import com.podcast.orchestration.dto.ExecutionResult;
import com.podcast.orchestration.enums.AgentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for Context Agent Decision Logic
 */
public class ContextAgentTest {
    
    private ContextAgent contextAgent;
    
    @BeforeEach
    public void setUp() {
        contextAgent = new ContextAgent();
    }
    
    /**
     * Test 1: First execution should return Backend Agent
     */
    @Test
    public void testFirstExecutionReturnsBackendAgent() {
        ContextDTO context = ContextDTO.builder()
            .episodeId(1L)
            .episodeTitle("Test Episode")
            .hostName("Host")
            .guestName("Guest")
            .guestExpertise("Tech")
            .guestBackground("Expert")
            .backendDataProcessed(false)
            .questionsGenerated(false)
            .scriptGenerated(false)
            .build();
        
        ExecutionResult result = contextAgent.buildAndDecideNextAgent(context, null);
        
        assertEquals(AgentType.BACKEND, result.getNextAgent());
        assertFalse(result.isTerminal());
    }
    
    /**
     * Test 2: User clicks "GENERATE_SCRIPT" without backend done
     * Should execute Backend first
     */
    @Test
    public void testGenerateScriptWithoutBackendExecutesBackendFirst() {
        ContextDTO context = ContextDTO.builder()
            .episodeId(1L)
            .episodeTitle("Test Episode")
            .backendDataProcessed(false)
            .questionsGenerated(false)
            .scriptGenerated(false)
            .build();
        
        ExecutionResult result = contextAgent.buildAndDecideNextAgent(context, "GENERATE_SCRIPT");
        
        // Should execute Backend first because it's a prerequisite
        assertEquals(AgentType.BACKEND, result.getNextAgent());
    }
    
    /**
     * Test 3: User clicks "GENERATE_SCRIPT" with backend already done
     * Should execute Script directly
     */
    @Test
    public void testGenerateScriptWithBackendDoneExecutesScriptDirectly() {
        ContextDTO context = ContextDTO.builder()
            .episodeId(1L)
            .episodeTitle("Test Episode")
            .backendDataProcessed(true)
            .questionsGenerated(false)
            .scriptGenerated(false)
            .build();
        
        ExecutionResult result = contextAgent.buildAndDecideNextAgent(context, "GENERATE_SCRIPT");
        
        // Should execute Script directly
        assertEquals(AgentType.SCRIPT, result.getNextAgent());
    }
    
    /**
     * Test 4: User clicks "GENERATE_QUESTIONS" without backend
     * Should execute Backend first
     */
    @Test
    public void testGenerateQuestionsWithoutBackendExecutesBackendFirst() {
        ContextDTO context = ContextDTO.builder()
            .episodeId(1L)
            .episodeTitle("Test Episode")
            .backendDataProcessed(false)
            .questionsGenerated(false)
            .scriptGenerated(false)
            .build();
        
        ExecutionResult result = contextAgent.buildAndDecideNextAgent(context, "GENERATE_QUESTIONS");
        
        // Should execute Backend first
        assertEquals(AgentType.BACKEND, result.getNextAgent());
    }
    
    /**
     * Test 5: Normal flow - Backend done, no user action
     * Should execute Questions next
     */
    @Test
    public void testNormalFlowReturnsQuestionBankAfterBackend() {
        ContextDTO context = ContextDTO.builder()
            .episodeId(1L)
            .episodeTitle("Test Episode")
            .backendDataProcessed(true)
            .questionsGenerated(false)
            .scriptGenerated(false)
            .build();
        
        ExecutionResult result = contextAgent.buildAndDecideNextAgent(context, null);
        
        // Should execute Questions
        assertEquals(AgentType.QUESTION_BANK, result.getNextAgent());
        assertFalse(result.isTerminal());
    }
    
    /**
     * Test 6: Backend and Questions done, no user action
     * Should execute Script next
     */
    @Test
    public void testNormalFlowReturnsScriptAfterQuestions() {
        ContextDTO context = ContextDTO.builder()
            .episodeId(1L)
            .episodeTitle("Test Episode")
            .backendDataProcessed(true)
            .questionsGenerated(true)
            .scriptGenerated(false)
            .build();
        
        ExecutionResult result = contextAgent.buildAndDecideNextAgent(context, null);
        
        // Should execute Script
        assertEquals(AgentType.SCRIPT, result.getNextAgent());
        assertFalse(result.isTerminal());
    }
    
    /**
     * Test 7: All agents executed
     * Should return terminal=true, nextAgent=null
     */
    @Test
    public void testAllAgentsExecutedReturnsTerminal() {
        ContextDTO context = ContextDTO.builder()
            .episodeId(1L)
            .episodeTitle("Test Episode")
            .backendDataProcessed(true)
            .questionsGenerated(true)
            .scriptGenerated(true)
            .build();
        
        ExecutionResult result = contextAgent.buildAndDecideNextAgent(context, null);
        
        // Should be terminal
        assertNull(result.getNextAgent());
        assertTrue(result.isTerminal());
    }
    
    /**
     * Test 8: Verify execution ID is generated
     */
    @Test
    public void testExecutionIdIsGenerated() {
        ContextDTO context = ContextDTO.builder()
            .episodeId(1L)
            .episodeTitle("Test Episode")
            .backendDataProcessed(false)
            .questionsGenerated(false)
            .scriptGenerated(false)
            .build();
        
        ExecutionResult result = contextAgent.buildAndDecideNextAgent(context, null);
        
        assertNotNull(result.getExecutionId());
        assertTrue(result.getExecutionId().length() > 0);
    }
}
