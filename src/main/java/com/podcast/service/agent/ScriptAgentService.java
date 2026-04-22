package com.podcast.service.agent;

import com.podcast.dto.EpisodeRequest;
import com.podcast.dto.EpisodeContext;
import com.podcast.dto.ScriptOutput;
import com.podcast.dto.ScriptSegment;
import com.podcast.dto.SegmentPlan;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ScriptAgentService {

    public ScriptOutput generateScript(EpisodeRequest request, EpisodeContext context) {
        ScriptOutput script = new ScriptOutput();
        
        script.setEpisode_title(generateTitle(request));
        script.setIntro(generateIntro(request));
        script.setSegments(generateSegments(request, context));
        script.setOutro(generateOutro(request));
        script.setEstimated_duration(request.getDuration_minutes());
        
        return script;
    }

    private String generateTitle(EpisodeRequest request) {
        if (request.getEpisode_title() != null && !request.getEpisode_title().isEmpty()) {
            return request.getEpisode_title();
        }
        return String.format("Episode: %s with %s", request.getTopic(), request.getGuest_name());
    }

    private String generateIntro(EpisodeRequest request) {
        return String.format(
            "Welcome to the podcast! Today we're discussing %s with our special guest %s, " +
            "a renowned %s. We'll explore insights on %s and what it means for our %s audience. " +
            "Let's dive in!",
            request.getTopic(),
            request.getGuest_name(),
            request.getGuest_title(),
            String.join(" and ", request.getGuest_expertise()),
            request.getAudience()
        );
    }

    private List<ScriptSegment> generateSegments(EpisodeRequest request, EpisodeContext context) {
        List<ScriptSegment> segments = new ArrayList<>();
        
        for (SegmentPlan plan : context.getSegment_plan()) {
            ScriptSegment segment = new ScriptSegment();
            segment.setName(plan.getName());
            segment.setHost_lines(generateHostLines(plan, request));
            segment.setTransition(generateTransition(plan));
            segments.add(segment);
        }
        
        return segments;
    }

    private List<String> generateHostLines(SegmentPlan plan, EpisodeRequest request) {
        List<String> lines = new ArrayList<>();
        
        if (plan.getName().contains("Introduction")) {
            lines.add(String.format("Thank you for joining us today, %s!", request.getGuest_name()));
            lines.add(String.format("You have an impressive background in %s. Tell us about your journey.",
                String.join(" and ", request.getGuest_expertise())));
        } else if (plan.getName().contains("Main")) {
            lines.add(String.format("Let's dig deeper into %s.", request.getTopic()));
            lines.add("What are the key challenges you see in this space?");
            lines.add("How do you approach solving these problems?");
        } else if (plan.getName().contains("Closing")) {
            lines.add("This has been incredibly insightful!");
            lines.add("What's one key takeaway you'd like our listeners to remember?");
        }
        
        return lines;
    }

    private String generateTransition(SegmentPlan plan) {
        return String.format("Now let's move on to the next part: %s", plan.getObjective());
    }

    private String generateOutro(EpisodeRequest request) {
        return String.format(
            "Thank you so much for your insights today, %s. " +
            "This conversation has been enlightening for our %s audience. " +
            "For more information and resources, visit our website. Thanks for listening!",
            request.getGuest_name(),
            request.getAudience()
        );
    }
}
