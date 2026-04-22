# 🚀 Quick Start Guide

## Prerequisites
- Java 17 or higher
- Maven 3.8 or higher
- Git (optional)

## 1. Build the Project

```bash
cd c:\Users\ChirumamillaNagaVenk\ai-kata
mvn clean install
```

Expected output:
```
[INFO] BUILD SUCCESS
```

## 2. Run the Application

```bash
mvn spring-boot:run
```

Or after building:
```bash
java -jar target/agent-orchestration-1.0.0.jar
```

You should see:
```
======================================================================
🎙️  PODCAST AGENT ORCHESTRATION SYSTEM STARTED
======================================================================
Available Endpoints:
  POST /api/orchestration/start           - Start new workflow
  POST /api/orchestration/generate-questions - Generate questions
  POST /api/orchestration/generate-script    - Generate script
  POST /api/orchestration/run-all            - Run all agents
  GET  /api/orchestration/health             - Health check
======================================================================
```

## 3. Test the API

### Health Check
```bash
curl -X GET http://localhost:8080/api/orchestration/health
```

### Start Workflow
```bash
curl -X POST http://localhost:8080/api/orchestration/start \
  -H "Content-Type: application/json" \
  -d '{
    "episodeId": 1,
    "episodeTitle": "AI in Healthcare",
    "hostName": "Sarah Johnson",
    "guestName": "Dr. Andrew Ng",
    "guestExpertise": "Machine Learning",
    "guestBackground": "AI researcher with 20+ years experience"
  }'
```

### Generate Questions
```bash
curl -X POST http://localhost:8080/api/orchestration/generate-questions \
  -H "Content-Type: application/json" \
  -d '{
    "episodeId": 1,
    "episodeTitle": "AI in Healthcare",
    "hostName": "Sarah Johnson",
    "guestName": "Dr. Andrew Ng",
    "guestExpertise": "Machine Learning",
    "guestBackground": "AI researcher",
    "backendDataProcessed": true,
    "questionsGenerated": false,
    "scriptGenerated": false
  }'
```

### Generate Script
```bash
curl -X POST http://localhost:8080/api/orchestration/generate-script \
  -H "Content-Type: application/json" \
  -d '{
    "episodeId": 1,
    "episodeTitle": "AI in Healthcare",
    "hostName": "Sarah Johnson",
    "guestName": "Dr. Andrew Ng",
    "guestExpertise": "Machine Learning",
    "guestBackground": "AI researcher",
    "backendDataProcessed": true,
    "questionsGenerated": true,
    "scriptGenerated": false
  }'
```

### Run All Agents (Continuous)
```bash
curl -X POST http://localhost:8080/api/orchestration/run-all \
  -H "Content-Type: application/json" \
  -d '{
    "episodeId": 2,
    "episodeTitle": "Quantum Computing",
    "hostName": "Michael Chen",
    "guestName": "Prof. Qiming Sun",
    "guestExpertise": "Quantum Computing",
    "guestBackground": "PhD in Physics"
  }'
```

## 4. View Execution History

Access H2 Database Console:
```
http://localhost:8080/h2-console
```

Default credentials:
- Driver: org.h2.Driver
- JDBC URL: jdbc:h2:mem:podcast_db
- User: sa
- Password: (leave blank)

Query execution history:
```sql
SELECT * FROM execution_history ORDER BY executed_at DESC;
```

## 5. Run Tests

```bash
mvn test
```

## Project Structure

```
ai-kata/
├── pom.xml                          (Maven configuration)
├── README.md                        (Full documentation)
├── ARCHITECTURE_EXPLANATION.md      (Design explanation)
├── API_EXAMPLES.md                  (API examples)
├── QUICK_START.md                   (This file)
│
├── src/main/java/com/podcast/orchestration/
│   ├── AgentOrchestrationApplication.java  (Main class)
│   │
│   ├── agent/
│   │   ├── Agent.java               (Interface)
│   │   └── impl/
│   │       ├── BackendAgent.java
│   │       ├── QuestionBankAgent.java
│   │       ├── ScriptAgent.java
│   │       └── ContextAgent.java     (Decision maker)
│   │
│   ├── controller/
│   │   └── OrchestrationController.java
│   │
│   ├── dto/
│   │   ├── ContextDTO.java
│   │   └── ExecutionResult.java
│   │
│   ├── entity/
│   │   └── ExecutionHistory.java
│   │
│   ├── enums/
│   │   └── AgentType.java
│   │
│   ├── repository/
│   │   └── ExecutionHistoryRepository.java
│   │
│   └── service/
│       └── AgentOrchestrationService.java
│
├── src/main/resources/
│   └── application.properties
│
└── src/test/java/com/podcast/orchestration/
    └── agent/impl/
        └── ContextAgentTest.java
```

## Key Files to Understand

1. **ContextAgent.java** - The decision maker
   - Where decision logic lives
   - Analyzes execution history
   - Returns next agent

2. **AgentOrchestrationService.java** - The orchestrator
   - Executes the decided agent
   - Tracks in database
   - Maintains agent map

3. **Individual Agents** - The workers
   - BackendAgent.java
   - QuestionBankAgent.java
   - ScriptAgent.java

4. **ContextDTO.java** - The context object
   - All data agents need
   - Tracks execution state
   - Updated by each agent

5. **ExecutionResult.java** - What Context Agent returns
   - Decision (nextAgent)
   - Updated context
   - Reason and metadata

## Common Issues & Solutions

### Issue: Port 8080 already in use
**Solution:**
```bash
# Change port in application.properties
server.port=8081
```

### Issue: Build fails with Java version
**Solution:**
Ensure Java 17+:
```bash
java -version
```

### Issue: Maven not found
**Solution:**
Add Maven to PATH or use:
```bash
./mvnw clean install  (on Linux/Mac)
mvnw.cmd clean install (on Windows)
```

## Next Steps

1. **Understand the flow**: Read ARCHITECTURE_EXPLANATION.md
2. **See API examples**: Check API_EXAMPLES.md
3. **Run tests**: `mvn test`
4. **Modify agents**: Add your own business logic
5. **Add database**: Replace H2 with PostgreSQL for production
6. **Add frontend**: Build React/Angular UI
7. **Add AI integration**: Connect to GPT/Claude APIs

## Debugging

### Enable detailed logging
In `application.properties`:
```properties
logging.level.com.podcast.orchestration=DEBUG
logging.level.org.springframework.web=DEBUG
```

### Check execution history
```bash
# Get all executions
SELECT * FROM execution_history;

# Get specific episode
SELECT * FROM execution_history WHERE episode_id = 1;

# Get execution order
SELECT executed_agent, executed_at, user_action 
FROM execution_history 
WHERE episode_id = 1 
ORDER BY execution_order;
```

### Monitor Spring Boot
Spring Boot Actuator endpoints (can be enabled):
```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/metrics
```

## Architecture Highlights

✅ **Separation of Concerns**
- Context Agent: Decision making
- Orchestrator: Execution
- Agents: Domain work

✅ **Clean Code**
- Clear interfaces
- Dependency injection
- Repository pattern

✅ **Testable Design**
- Each component testable independently
- Mock-friendly
- No hidden dependencies

✅ **Scalable**
- Adding new agents is simple
- Decision logic is centralized
- Loose coupling

## For Your Interview

**Key points to mention:**
1. Context Agent decides, Orchestrator executes
2. Execution history drives decisions
3. User interaction can override default flow
4. Each agent is independent
5. System is testable and scalable

**Demo flow:**
1. Start workflow → Backend executes
2. Click "Generate Questions" → Questions executes
3. Click "Generate Script" → Script executes
4. Show execution history in database

---

**Happy coding! 🚀**

For full documentation, see README.md and ARCHITECTURE_EXPLANATION.md
