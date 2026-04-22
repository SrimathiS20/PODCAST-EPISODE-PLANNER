package com.podcast.dto;

public class Question {
    private String question;
    private String purpose;
    private String follow_up;

    public Question() {}

    public Question(String question, String purpose, String follow_up) {
        this.question = question;
        this.purpose = purpose;
        this.follow_up = follow_up;
    }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getFollow_up() { return follow_up; }
    public void setFollow_up(String follow_up) { this.follow_up = follow_up; }
}
