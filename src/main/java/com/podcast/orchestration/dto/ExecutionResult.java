package com.podcast.orchestration.dto;

import com.podcast.orchestration.enums.AgentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ExecutionResult - What Context Agent returns after making a decision
 * Contains: the decision (next agent) + updated context
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionResult implements Serializable {
    
    // The decision made by Context Agent
    private AgentType nextAgent;
    
    // Updated context for the next agent to use
    private ContextDTO contextDTO;
    
    // Execution metadata
    private String executionId;
    private LocalDateTime timestamp;
    private String reason; // Why this agent was chosen
    
    // Status information
    private boolean isTerminal; // true if no more agents to execute
    private String status; // SUCCESS, PENDING, ERROR
}
