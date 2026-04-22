package com.podcast.orchestration.entity;

import com.podcast.orchestration.enums.AgentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity to track execution history
 * Used by Context Agent to make decisions
 */
@Entity
@Table(name = "execution_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Session/Episode reference
    @Column(nullable = false)
    private Long episodeId;
    
    // Which agent executed
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgentType executedAgent;
    
    // Execution details
    private String executionId;
    private LocalDateTime executedAt;
    private String result; // JSON or description of what was generated
    private String status; // SUCCESS, FAILED
    
    // User action that triggered this (if applicable)
    private String userAction;
    
    // Order of execution
    private Integer executionOrder;
}
