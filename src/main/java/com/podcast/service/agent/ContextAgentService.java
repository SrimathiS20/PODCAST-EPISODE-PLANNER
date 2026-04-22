package com.podcast.service.agent;

import com.podcast.dto.EpisodeRequest;
import com.podcast.dto.EpisodeContext;
import com.podcast.dto.SegmentPlan;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class ContextAgentService {

    public EpisodeContext generateContext(EpisodeRequest request) {
        EpisodeContext context = new EpisodeContext();
        
        context.setTitle_hint(generateTitleHint(request));
        context.setAngle(generateAngle(request));
        context.setKeywords(generateKeywords(request));
        context.setConstraints(generateConstraints(request));
        context.setGuest_intro_line(generateGuestIntro(request));
        context.setSegment_plan(generateSegmentPlan(request));
        
        return context;
    }

    private String generateTitleHint(EpisodeRequest request) {
        return String.format("%s with %s: Deep Dive into %s", 
            request.getGuest_name(), 
            request.getGuest_title(), 
            request.getTopic());
    }

    private String generateAngle(EpisodeRequest request) {
        return String.format("Exploring %s from the perspective of a seasoned %s with expertise in %s",
            request.getTopic(),
            request.getGuest_title(),
            String.join(", ", request.getGuest_expertise()));
    }

    private List<String> generateKeywords(EpisodeRequest request) {
        return Arrays.asList(
            request.getTopic(),
            request.getGuest_name(),
            request.getGuest_title(),
            String.join(", ", request.getGuest_expertise()),
            request.getAudience()
        );
    }

    private List<String> generateConstraints(EpisodeRequest request) {
        return Arrays.asList(
            "Keep tone: " + request.getTone(),
            "Total duration: " + request.getDuration_minutes() + " minutes",
            "Target audience: " + request.getAudience(),
            "Include guest expertise areas: " + String.join(", ", request.getGuest_expertise())
        );
    }

    private String generateGuestIntro(EpisodeRequest request) {
        return String.format("Please welcome %s, a %s with extensive experience in %s. %s",
            request.getGuest_name(),
            request.getGuest_title(),
            String.join(", ", request.getGuest_expertise()),
            request.getGuest_bio());
    }

    private List<SegmentPlan> generateSegmentPlan(EpisodeRequest request) {
        int duration = request.getDuration_minutes();
        int introTime = Math.max(2, duration / 10);
        int mainTime = duration - introTime - 3;
        
        return Arrays.asList(
            new SegmentPlan("Introduction & Guest Bio", introTime, 
                "Introduce host and guest, establish rapport"),
            new SegmentPlan("Main Topic Discussion", mainTime, 
                "Deep dive into " + request.getTopic() + " with guest insights"),
            new SegmentPlan("Closing & Resources", 3, 
                "Summarize key takeaways and provide next steps")
        );
    }
}
