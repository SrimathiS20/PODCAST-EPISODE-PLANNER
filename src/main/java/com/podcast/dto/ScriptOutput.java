package com.podcast.dto;

import java.util.List;

public class ScriptOutput {
    private String episode_title;
    private String intro;
    private List<ScriptSegment> segments;
    private String outro;
    private Integer estimated_duration;

    public ScriptOutput() {}

    public ScriptOutput(String episode_title, String intro, List<ScriptSegment> segments, String outro, Integer estimated_duration) {
        this.episode_title = episode_title;
        this.intro = intro;
        this.segments = segments;
        this.outro = outro;
        this.estimated_duration = estimated_duration;
    }

    public String getEpisode_title() { return episode_title; }
    public void setEpisode_title(String episode_title) { this.episode_title = episode_title; }

    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }

    public List<ScriptSegment> getSegments() { return segments; }
    public void setSegments(List<ScriptSegment> segments) { this.segments = segments; }

    public String getOutro() { return outro; }
    public void setOutro(String outro) { this.outro = outro; }

    public Integer getEstimated_duration() { return estimated_duration; }
    public void setEstimated_duration(Integer estimated_duration) { this.estimated_duration = estimated_duration; }
}
