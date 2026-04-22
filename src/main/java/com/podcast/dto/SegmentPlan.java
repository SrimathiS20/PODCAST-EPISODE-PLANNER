package com.podcast.dto;

public class SegmentPlan {
    private String name;
    private Integer minutes;
    private String objective;

    public SegmentPlan() {}

    public SegmentPlan(String name, Integer minutes, String objective) {
        this.name = name;
        this.minutes = minutes;
        this.objective = objective;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getMinutes() { return minutes; }
    public void setMinutes(Integer minutes) { this.minutes = minutes; }

    public String getObjective() { return objective; }
    public void setObjective(String objective) { this.objective = objective; }
}
