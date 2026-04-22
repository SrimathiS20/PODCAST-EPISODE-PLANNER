package com.podcast.dto;

import java.util.List;

public class EpisodeContext {
    private String title_hint;
    private String angle;
    private List<String> keywords;
    private List<String> constraints;
    private String guest_intro_line;
    private List<SegmentPlan> segment_plan;

    public EpisodeContext() {}

    public EpisodeContext(String title_hint, String angle, List<String> keywords, List<String> constraints, String guest_intro_line, List<SegmentPlan> segment_plan) {
        this.title_hint = title_hint;
        this.angle = angle;
        this.keywords = keywords;
        this.constraints = constraints;
        this.guest_intro_line = guest_intro_line;
        this.segment_plan = segment_plan;
    }

    public String getTitle_hint() { return title_hint; }
    public void setTitle_hint(String title_hint) { this.title_hint = title_hint; }

    public String getAngle() { return angle; }
    public void setAngle(String angle) { this.angle = angle; }

    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }

    public List<String> getConstraints() { return constraints; }
    public void setConstraints(List<String> constraints) { this.constraints = constraints; }

    public String getGuest_intro_line() { return guest_intro_line; }
    public void setGuest_intro_line(String guest_intro_line) { this.guest_intro_line = guest_intro_line; }

    public List<SegmentPlan> getSegment_plan() { return segment_plan; }
    public void setSegment_plan(List<SegmentPlan> segment_plan) { this.segment_plan = segment_plan; }
}
