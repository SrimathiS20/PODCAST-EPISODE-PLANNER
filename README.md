# 🎙️ Agent Orchestration System for Podcast Production

A sophisticated multi-agent orchestration system that demonstrates clean architecture, separation of concerns, and intelligent decision-making based on execution history.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (UI)                            │
│          User clicks: "Generate Questions"                  │
│                     or "Generate Script"                    │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│           REST Controller (Orchestration)                   │
│            Receives user actions                            │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│    🧠 CONTEXT AGENT (Decision Maker - Brain)               │
│                                                             │
│  Responsibilities:                                          │
│  ✓ Builds ContextDTO                                       │
│  ✓ Tracks execution history                                │
│  ✓ Decides NEXT AGENT based on:                            │
│    - Execution history (what agents ran)                    │
│    - User interaction (what user clicked)                   │
│                                                             │
│  Returns: {nextAgent, contextDTO, reason}                  │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│  🤖 ORCHESTRATOR (Executor - Hands)                         │
│                                                             │
│  Responsibilities:                                          │
│  ✓ Receives ExecutionResult from Context Agent             │
│  ✓ Executes the decided agent                              │
│  ✓ Tracks execution in database                            │
│  ✓ Returns updated context                                 │
│                                                             │
│  switch(result.getNextAgent()) {                           │
│    case BACKEND:       → backendAgent.execute()            │
│    case QUESTION_BANK: → questionBankAgent.execute()       │
│    case SCRIPT:        → scriptAgent.execute()             │
│  }                                                          │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│              👷 SPECIALIZED AGENTS                          │
│                                                             │
│  ├─ Backend Agent:                                         │
│  │  └─ Process episode data, prepare backend context       │
│  │                                                         │
│  ├─ Question Bank Agent:                                   │
│  │  └─ Generate interview questions based on expertise     │
│  │                                                         │
│  └─ Script Agent:                                          │
│     └─ Generate complete podcast script                    │
└─────────────────────────────────────────────────────────────┘
```

## Key Components

### 1. **Context Agent (Decision Maker)**
- **File**: `agent/impl/ContextAgent.java`
- **Role**: Brain of the system
- **Decisions**: Which agent executes next based on:
  - Execution history (Backend done? Questions done? Script done?)
  - User interaction (Did user click "Generate Script"?)
  
```
Decision Logic:
├─ If user clicks "GENERATE_SCRIPT"
│  ├─ Check if Backend done
│  ├─ If not → Run Backend first, then Script
│  └─ If yes → Run Script directly
├─ If user clicks "GENERATE_QUESTIONS"
│  ├─ Check if Backend done
│  ├─ If not → Run Backend first, then Questions
│  └─ If yes → Run Questions
└─ Default flow: Backend → Questions → Script
```

### 2. **Orchestrator (Executor)**
- **File**: `service/AgentOrchestrationService.java`
- **Role**: Hands of the system - executes agents
- **Process**:
  1. Receives ExecutionResult from Context Agent
  2. Gets the decided agent from result
  3. Executes that agent
  4. Tracks execution in database
  5. Returns updated context

### 3. **Agent Implementations**

#### Backend Agent
- Processes episode and guest data
- Prepares backend context
- Builds prompts for other agents
- **File**: `agent/impl/BackendAgent.java`

#### Question Bank Agent
- Generates interview questions
- Uses guest expertise to create targeted questions
- Requires Backend to be executed first
- **File**: `agent/impl/QuestionBankAgent.java`

#### Script Agent
- Generates complete podcast script
- Uses questions and backend context
- Can execute independently but works better with questions
- **File**: `agent/impl/ScriptAgent.java`

### 4. **Data Structures**

#### ContextDTO
- Contains all data needed by agents
- Tracks which agents have executed
- Holds generated prompts

#### ExecutionResult
- Returned by Context Agent
- Contains:
  - `nextAgent`: Which agent to execute
  - `contextDTO`: Updated context
  - `reason`: Why this agent was chosen
  - `isTerminal`: Whether workflow is complete

#### ExecutionHistory
- Persisted in database
- Tracks: which agent, when, why (user action)
- Used for decision-making

## Execution Flow

### Scenario 1: User clicks "Generate Questions"
```
1. Frontend sends request to /api/orchestration/generate-questions
2. Controller passes ContextDTO + userAction="GENERATE_QUESTIONS"
3. Context Agent checks:
   - Is Backend done? NO
   - → Returns nextAgent = BACKEND
4. Orchestrator executes Backend Agent
5. Context updates: backendDataProcessed = true
6. Next cycle: Context Agent checks again
   - Is Backend done? YES
   - User wants questions? YES
   - → Returns nextAgent = QUESTION_BANK
7. Orchestrator executes Question Bank Agent
8. Context updates: questionsGenerated = true
9. Done!
```

### Scenario 2: User clicks "Generate Script"
```
1. Frontend sends request to /api/orchestration/generate-script
2. Controller passes ContextDTO + userAction="GENERATE_SCRIPT"
3. Context Agent checks:
   - Is Backend done? NO
   - → Returns nextAgent = BACKEND
4. Orchestrator executes Backend Agent
5. Next cycle (automatic in continuous mode):
   - Context Agent checks: Backend done? YES, Script done? NO
   - → Returns nextAgent = SCRIPT
6. Orchestrator executes Script Agent
7. Done!
```

## Architecture Benefits

### ✅ Separation of Concerns
- **Context Agent** = Decision making logic only
- **Orchestrator** = Execution logic only
- **Agents** = Domain-specific work

### ✅ Scalability
- Adding new agents is simple: implement Agent interface
- Modify decision logic in Context Agent
- Orchestrator delegates execution

### ✅ Testability
- Each component can be tested independently
- Mock agents easily
- Test decision logic separately from execution

### ✅ Maintainability
- Clear responsibility for each component
- Easy to understand the flow
- Loose coupling between components

### ✅ Traceability
- Execution history tracked in database
- Can replay workflow
- Can analyze decision patterns

## API Endpoints

### Start Workflow
```
POST /api/orchestration/start
Body: {
  "episodeId": 1,
  "episodeTitle": "AI in Healthcare",
  "hostName": "John Doe",
  "guestName": "Dr. Jane Smith",
  "guestExpertise": "Machine Learning in Healthcare",
  "guestBackground": "10 years in medical AI"
}
Response: ExecutionResult with first agent to execute
```

### Generate Questions
```
POST /api/orchestration/generate-questions
Body: ContextDTO
Response: ExecutionResult
```

### Generate Script
```
POST /api/orchestration/generate-script
Body: ContextDTO
Response: ExecutionResult
```

### Run All
```
POST /api/orchestration/run-all
Body: ContextDTO
Response: ExecutionResult (terminal=true)
```

## Running the Application

### Prerequisites
- Java 17+
- Maven 3.8+

### Build
```bash
mvn clean package
```

### Run
```bash
mvn spring-boot:run
```

Application runs on: `http://localhost:8080`

### Test Endpoint
```bash
curl -X GET http://localhost:8080/api/orchestration/health
```

## Database
- Uses H2 in-memory database
- Auto-creates tables on startup
- H2 Console available at: `http://localhost:8080/h2-console`

## Key Design Patterns

1. **Strategy Pattern**: Agent implementations (BackendAgent, QuestionBankAgent, ScriptAgent)
2. **Factory Pattern**: AgentMap in Orchestrator
3. **Repository Pattern**: ExecutionHistoryRepository
4. **Service Layer Pattern**: AgentOrchestrationService
5. **DTO Pattern**: ContextDTO, ExecutionResult

## How This Solves the Interview Question

**"Context Agent decides AND controls execution"** ❌

**vs.**

**"Context Agent decides, Orchestrator executes"** ✅

This implementation shows:
- ✅ **Separation of Concerns**: Decision (Context) vs. Execution (Orchestrator)
- ✅ **Loose Coupling**: Agents don't need to know about each other
- ✅ **High Cohesion**: Each component has a single responsibility
- ✅ **Scalability**: Easy to add new agents or modify decision logic
- ✅ **Clean Architecture**: Clear layering and dependencies

### Confident Answer for Viva:

> "The Context Agent builds the context and decides the next agent based on execution history and user interaction, while the orchestrator layer handles the actual execution of agents. This separation maintains loose coupling and ensures scalability."

## Project Structure
```
src/
├── main/
│   ├── java/com/podcast/orchestration/
│   │   ├── agent/
│   │   │   ├── Agent.java (interface)
│   │   │   └── impl/
│   │   │       ├── BackendAgent.java
│   │   │       ├── QuestionBankAgent.java
│   │   │       ├── ScriptAgent.java
│   │   │       └── ContextAgent.java
│   │   ├── controller/
│   │   │   └── OrchestrationController.java
│   │   ├── dto/
│   │   │   ├── ContextDTO.java
│   │   │   └── ExecutionResult.java
│   │   ├── entity/
│   │   │   └── ExecutionHistory.java
│   │   ├── enums/
│   │   │   └── AgentType.java
│   │   ├── repository/
│   │   │   └── ExecutionHistoryRepository.java
│   │   ├── service/
│   │   │   └── AgentOrchestrationService.java
│   │   └── AgentOrchestrationApplication.java
│   └── resources/
│       └── application.properties
└── test/ (add your tests here)
```

## Next Steps

1. **Add AI Service Integration**: Connect to actual AI APIs (GPT, etc.)
2. **Add Tests**: Unit and integration tests for all components
3. **Add Authentication**: Secure the endpoints
4. **Add Frontend**: Build React/Angular UI
5. **Add Monitoring**: Logging, metrics, alerting
6. **Add Caching**: Cache decision results
7. **Add Retry Logic**: Handle agent failures

---

**Built with ❤️ for clean architecture and intelligent orchestration**
