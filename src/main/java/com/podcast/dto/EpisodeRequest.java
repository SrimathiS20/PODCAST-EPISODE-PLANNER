package com.podcast.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class EpisodeRequest {

    @NotBlank(message = "Topic is required")
    private String topic;

    private String episode_title;

    @NotBlank(message = "Guest name is required")
    private String guest_name;

    @NotBlank(message = "Guest title is required")
    private String guest_title;

    @NotBlank(message = "Guest bio is required")
    private String guest_bio;

    @NotEmpty(message = "Guest expertise list cannot be empty")
    private List<String> guest_expertise;

    @NotBlank(message = "Audience is required")
    private String audience;

    @NotBlank(message = "Tone is required")
    private String tone;

    @NotNull(message = "Duration in minutes is required")
    @Min(value = 5, message = "Duration must be at least 5 minutes")
    @Max(value = 480, message = "Duration cannot exceed 480 minutes (8 hours)")
    private Integer duration_minutes;

    public EpisodeRequest() {}

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getEpisode_title() { return episode_title; }
    public void setEpisode_title(String episode_title) { this.episode_title = episode_title; }

    public String getGuest_name() { return guest_name; }
    public void setGuest_name(String guest_name) { this.guest_name = guest_name; }

    public String getGuest_title() { return guest_title; }
    public void setGuest_title(String guest_title) { this.guest_title = guest_title; }

    public String getGuest_bio() { return guest_bio; }
    public void setGuest_bio(String guest_bio) { this.guest_bio = guest_bio; }

    public List<String> getGuest_expertise() { return guest_expertise; }
    public void setGuest_expertise(List<String> guest_expertise) { this.guest_expertise = guest_expertise; }

    public String getAudience() { return audience; }
    public void setAudience(String audience) { this.audience = audience; }

    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }

    public Integer getDuration_minutes() { return duration_minutes; }
    public void setDuration_minutes(Integer duration_minutes) { this.duration_minutes = duration_minutes; }
}
