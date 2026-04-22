package com.podcast.service.agent;

import com.podcast.dto.EpisodeRequest;
import com.podcast.dto.QuestionBankOutput;
import com.podcast.dto.Question;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class QuestionBankAgentService {

    public QuestionBankOutput generateQuestionBank(EpisodeRequest request) {
        QuestionBankOutput questionBank = new QuestionBankOutput();
        
        questionBank.setWarmup(generateWarmupQuestions(request));
        questionBank.setDeep_dive(generateDeepDiveQuestions(request));
        questionBank.setClosing(generateClosingQuestions(request));
        questionBank.setRapid_fire(generateRapidFireQuestions(request));
        
        return questionBank;
    }

    private List<Question> generateWarmupQuestions(EpisodeRequest request) {
        return Arrays.asList(
            new Question(
                String.format("Can you walk us through your background and how you got into %s?",
                    request.getGuest_expertise().get(0)),
                "Establish rapport and set comfortable tone",
                String.format("What were some of the turning points early in your %s career?",
                    request.getGuest_expertise().get(0))
            ),
            new Question(
                String.format("What does %s mean to you personally?", request.getTopic()),
                "Connect with audience on personal level",
                "Can you share a memorable experience that shaped your perspective?"
            ),
            new Question(
                String.format("For our %s audience, why is understanding %s important right now?",
                    request.getAudience(), request.getTopic()),
                "Bridge guest expertise with audience needs",
                "What's changing in this space that people should be aware of?"
            )
        );
    }

    private List<Question> generateDeepDiveQuestions(EpisodeRequest request) {
        return Arrays.asList(
            new Question(
                String.format("What are the biggest misconceptions about %s that you encounter?",
                    request.getTopic()),
                "Clarify common misunderstandings",
                "How do these misconceptions impact decision-making?"
            ),
            new Question(
                String.format("With your expertise in %s, what frameworks or methodologies do you use?",
                    String.join(" and ", request.getGuest_expertise())),
                "Provide practical, actionable insights",
                "Can you walk us through a real-world example?"
            ),
            new Question(
                String.format("What's the intersection between %s and your other areas of expertise?",
                    request.getTopic()),
                "Explore complexity and interconnections",
                "How do you balance these different domains?"
            ),
            new Question(
                "What are the biggest challenges you see ahead in this space?",
                "Discuss future trends and obstacles",
                "How are you preparing for these changes?"
            )
        );
    }

    private List<Question> generateClosingQuestions(EpisodeRequest request) {
        return Arrays.asList(
            new Question(
                String.format("What's one piece of advice you'd give to someone entering the %s field?",
                    request.getGuest_expertise().get(0)),
                "Provide mentorship and guidance",
                "What skills should they focus on developing?"
            ),
            new Question(
                String.format("If you could solve one major problem in %s, what would it be?",
                    request.getTopic()),
                "Understand core passion and vision",
                "What's your plan or dream for achieving that?"
            ),
            new Question(
                "What are you currently working on or excited about?",
                "Create forward-looking momentum",
                "Where can listeners learn more about your work?"
            )
        );
    }

    private List<Question> generateRapidFireQuestions(EpisodeRequest request) {
        return Arrays.asList(
            new Question(
                "What's your favorite tool or resource?",
                "Quick, personal insight",
                "Why do you prefer it over alternatives?"
            ),
            new Question(
                "Coffee or tea?",
                "Light-hearted personality reveal",
                "What's your usual routine?"
            ),
            new Question(
                String.format("Book, podcast, or %s content you recommend?", request.getTopic()),
                "Provide listener recommendations",
                "What makes it stand out?"
            ),
            new Question(
                "What's a failure that shaped you?",
                "Vulnerability and growth mindset",
                "What did you learn from it?"
            ),
            new Question(
                "Final message for our listeners?",
                "Inspirational closing",
                null
            )
        );
    }
}
