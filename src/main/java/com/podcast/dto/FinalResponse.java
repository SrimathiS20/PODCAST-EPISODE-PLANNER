package com.podcast.dto;

public class FinalResponse {
    private EpisodeContext context;
    private ScriptOutput script;
    private QuestionBankOutput question_bank;

    public FinalResponse() {}

    public FinalResponse(EpisodeContext context, ScriptOutput script, QuestionBankOutput question_bank) {
        this.context = context;
        this.script = script;
        this.question_bank = question_bank;
    }

    public EpisodeContext getContext() { return context; }
    public void setContext(EpisodeContext context) { this.context = context; }

    public ScriptOutput getScript() { return script; }
    public void setScript(ScriptOutput script) { this.script = script; }

    public QuestionBankOutput getQuestion_bank() { return question_bank; }
    public void setQuestion_bank(QuestionBankOutput question_bank) { this.question_bank = question_bank; }
}
