package com.podcast.service;

import com.podcast.dto.EpisodeRequest;
import com.podcast.dto.FinalResponse;
import com.podcast.service.agent.ContextAgentService;
import com.podcast.service.agent.ScriptAgentService;
import com.podcast.service.agent.QuestionBankAgentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GenerateService {

    private final ContextAgentService contextAgentService;
    private final ScriptAgentService scriptAgentService;
    private final QuestionBankAgentService questionBankAgentService;
    private final List<FinalResponse> history = Collections.synchronizedList(new ArrayList<>());

    public GenerateService(ContextAgentService contextAgentService,
                           ScriptAgentService scriptAgentService,
                           QuestionBankAgentService questionBankAgentService) {
        this.contextAgentService = contextAgentService;
        this.scriptAgentService = scriptAgentService;
        this.questionBankAgentService = questionBankAgentService;
    }

    public List<FinalResponse> getAllResponses() {
        return history;
    }

    public FinalResponse getLatestResponse() {
        if (history.isEmpty()) return null;
        return history.get(history.size() - 1);
    }

    public FinalResponse generateEpisode(EpisodeRequest request) {
        // Step 1: Generate context from ContextAgent
        var context = contextAgentService.generateContext(request);

        // Step 2: Generate script from ScriptAgent (uses context)
        var script = scriptAgentService.generateScript(request, context);

        // Step 3: Generate question bank from QuestionBankAgent
        var questionBank = questionBankAgentService.generateQuestionBank(request);

        // Step 4: Combine all outputs into FinalResponse
        FinalResponse response = new FinalResponse(context, script, questionBank);
        history.add(response);
        return response;
    }
}
