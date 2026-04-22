package com.podcast.dto;

import java.util.List;

public class QuestionBankOutput {
    private List<Question> warmup;
    private List<Question> deep_dive;
    private List<Question> closing;
    private List<Question> rapid_fire;

    public QuestionBankOutput() {}

    public QuestionBankOutput(List<Question> warmup, List<Question> deep_dive, List<Question> closing, List<Question> rapid_fire) {
        this.warmup = warmup;
        this.deep_dive = deep_dive;
        this.closing = closing;
        this.rapid_fire = rapid_fire;
    }

    public List<Question> getWarmup() { return warmup; }
    public void setWarmup(List<Question> warmup) { this.warmup = warmup; }

    public List<Question> getDeep_dive() { return deep_dive; }
    public void setDeep_dive(List<Question> deep_dive) { this.deep_dive = deep_dive; }

    public List<Question> getClosing() { return closing; }
    public void setClosing(List<Question> closing) { this.closing = closing; }

    public List<Question> getRapid_fire() { return rapid_fire; }
    public void setRapid_fire(List<Question> rapid_fire) { this.rapid_fire = rapid_fire; }
}
