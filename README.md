# Podcast Episode Planner & Script Assistant - Backend

A Spring Boot backend for generating complete podcast episode planning content including context, scripts, and question banks.

## Project Structure

```
podcast-planner/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/podcast/
│   │   │       ├── PodcastPlannerApplication.java
│   │   │       ├── controller/
│   │   │       │   └── GenerateController.java
│   │   │       ├── service/
│   │   │       │   ├── GenerateService.java
│   │   │       │   └── agent/
│   │   │       │       ├── ContextAgentService.java
│   │   │       │       ├── ScriptAgentService.java
│   │   │       │       └── QuestionBankAgentService.java
│   │   │       └── dto/
│   │   │           ├── EpisodeRequest.java
│   │   │           ├── FinalResponse.java
│   │   │           ├── EpisodeContext.java
│   │   │           ├── SegmentPlan.java
│   │   │           ├── ScriptOutput.java
│   │   │           ├── ScriptSegment.java
│   │   │           ├── QuestionBankOutput.java
│   │   │           └── Question.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Database**: H2 (in-memory)
- **ORM**: JPA/Hibernate
- **Build**: Maven
- **Java Version**: 17
- **Additional**: Lombok for reducing boilerplate

## Key Components

### Controllers
- **GenerateController**: REST endpoint at `/api/generate` (POST)

### Services
- **GenerateService**: Orchestrates the complete flow
- **ContextAgentService**: Generates episode context, planning, and structure
- **ScriptAgentService**: Generates podcast script and dialogue
- **QuestionBankAgentService**: Generates interview questions for all phases

### DTOs (Data Transfer Objects)
- **EpisodeRequest**: Input request with episode details
- **FinalResponse**: Combined output with context, script, and questions
- Nested DTOs: EpisodeContext, ScriptOutput, QuestionBankOutput, Question, SegmentPlan, ScriptSegment

## API Endpoints

### Generate Complete Episode
```
POST /api/generate

Request Body:
{
  "topic": "AI in Healthcare",
  "episode_title": "Machine Learning Transforming Medicine",
  "guest_name": "Dr. John Smith",
  "guest_title": "AI Research Director",
  "guest_bio": "10+ years in medical AI research",
  "guest_expertise": ["Machine Learning", "Healthcare", "Medical Devices"],
  "audience": "Healthcare Professionals",
  "tone": "Professional but conversational",
  "duration_minutes": 45
}

Response:
{
  "context": {
    "title_hint": "...",
    "angle": "...",
    "keywords": [...],
    "constraints": [...],
    "guest_intro_line": "...",
    "segment_plan": [...]
  },
  "script": {
    "episode_title": "...",
    "intro": "...",
    "segments": [...],
    "outro": "...",
    "estimated_duration": 45
  },
  "question_bank": {
    "warmup": [...],
    "deep_dive": [...],
    "closing": [...],
    "rapid_fire": [...]
  }
}
```

## Running the Application

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### H2 Console (for debugging)
Access at: `http://localhost:8080/h2-console`

## Architecture Principles

- **SOLID**: Single responsibility, Open/closed, Liskov, Interface segregation, Dependency inversion
- **KISS**: Keep It Simple, Stupid - no overengineering
- **DRY**: Don't Repeat Yourself - reusable components
- **YAGNI**: You Aren't Gonna Need It - only essential features

## Testing the API

Use curl or Postman:

```bash
curl -X POST http://localhost:8080/api/generate \
  -H "Content-Type: application/json" \
  -d '{
    "topic": "AI in Healthcare",
    "guest_name": "Dr. John Smith",
    "guest_title": "AI Research Director",
    "guest_bio": "10+ years in medical AI",
    "guest_expertise": ["Machine Learning", "Healthcare"],
    "audience": "Healthcare Professionals",
    "tone": "Professional",
    "duration_minutes": 45
  }'
```

## Notes

- Episode and Guest entities are optional (can be extended when needed)
- Mock data is generated based on the input request
- No authentication is currently implemented
- H2 database is configured for demo purposes (in-memory, non-persistent)
