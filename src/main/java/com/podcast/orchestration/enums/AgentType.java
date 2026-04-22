package com.podcast.orchestration.enums;

/**
 * Enum for Agent Types in the orchestration system
 */
public enum AgentType {
    FRONTEND("Frontend - User Input Handler"),
    BACKEND("Backend - Data Processing Agent"),
    QUESTION_BANK("Question Bank - Question Generation Agent"),
    SCRIPT("Script - Script Generation Agent"),
    CONTEXT("Context Agent - Decision Maker");

    private final String description;

    AgentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
