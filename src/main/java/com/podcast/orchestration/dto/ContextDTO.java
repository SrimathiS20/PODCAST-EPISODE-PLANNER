package com.podcast.orchestration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Map;

/**
 * Data Transfer Object for Context information
 * Contains all data needed by agents to execute
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContextDTO implements Serializable {
    
    // Episode information
    private Long episodeId;
    private String episodeTitle;
    private String hostName;
    
    // Guest information
    private String guestName;
    private String guestExpertise;
    private String guestBackground;
    
    // Prompts for different agents
    private String backendPrompt;
    private String questionBankPrompt;
    private String scriptPrompt;
    
    // Additional context data
    private Map<String, Object> additionalData;
    
    // Track what has been generated
    private boolean backendDataProcessed;
    private boolean questionsGenerated;
    private boolean scriptGenerated;
}
