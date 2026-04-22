package com.podcast.orchestration.repository;

import com.podcast.orchestration.entity.ExecutionHistory;
import com.podcast.orchestration.enums.AgentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ExecutionHistory entity
 * Used to track and query execution history
 */
@Repository
public interface ExecutionHistoryRepository extends JpaRepository<ExecutionHistory, Long> {
    
    List<ExecutionHistory> findByEpisodeIdOrderByExecutionOrderAsc(Long episodeId);
    
    List<ExecutionHistory> findByEpisodeIdAndExecutedAgent(Long episodeId, AgentType agentType);
    
    ExecutionHistory findFirstByEpisodeIdOrderByExecutionOrderDesc(Long episodeId);
}
