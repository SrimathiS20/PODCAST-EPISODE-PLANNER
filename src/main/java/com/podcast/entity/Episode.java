package com.podcast.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "episodes")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    private String episodeTitle;

    @Column(nullable = false)
    private String guestName;

    private String guestTitle;

    @Column(length = 1000)
    private String guestBio;

    private String audience;

    private String tone;

    private Integer durationMinutes;

    public Episode() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getEpisodeTitle() { return episodeTitle; }
    public void setEpisodeTitle(String episodeTitle) { this.episodeTitle = episodeTitle; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getGuestTitle() { return guestTitle; }
    public void setGuestTitle(String guestTitle) { this.guestTitle = guestTitle; }

    public String getGuestBio() { return guestBio; }
    public void setGuestBio(String guestBio) { this.guestBio = guestBio; }

    public String getAudience() { return audience; }
    public void setAudience(String audience) { this.audience = audience; }

    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
}
