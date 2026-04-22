package com.podcast.orchestration.agent;

import com.podcast.orchestration.dto.ContextDTO;

/**
 * Base Agent Interface
 * All agents must implement this contract
 */
public interface Agent {
    
    /**
     * Execute the agent with given context
     * @param context The context data
     * @return Updated context with agent's results
     */
    ContextDTO execute(ContextDTO context);
    
    /**
     * Get the name of this agent
     */
    String getAgentName();
    
    /**
     * Check if this agent can execute given the current context
     */
    boolean canExecute(ContextDTO context);
}
