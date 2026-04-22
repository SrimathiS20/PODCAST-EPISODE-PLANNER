# 🎯 Visual Flow Diagrams

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                         FRONTEND (UI)                               │
│                                                                     │
│  User clicks: "Start" / "Generate Questions" / "Generate Script"   │
└──────────────────────────┬──────────────────────────────────────────┘
                           │
                           │ HTTP Request + ContextDTO + userAction
                           ▼
┌──────────────────────────────────────────────────────────────────────┐
│            REST Controller (Entry Point)                             │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │ @PostMapping("/start")                                       │ │
│  │ @PostMapping("/generate-questions")                          │ │
│  │ @PostMapping("/generate-script")                             │ │
│  │ @PostMapping("/run-all")                                     │ │
│  └────────┬───────────────────────────────────────────────────────┘ │
└───────────┼──────────────────────────────────────────────────────────┘
            │
            │ calls orchestrationService.orchestrate()
            ▼
┌──────────────────────────────────────────────────────────────────────┐
│  🧠 Context Agent (THE BRAIN - DECISION MAKER)                       │
│                                                                      │
│  Method: buildAndDecideNextAgent(context, userAction)               │
│                                                                      │
│  ANALYSIS:                                                           │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │ 1. Check userAction:                                         │  │
│  │    - "GENERATE_SCRIPT" → Want script?                        │  │
│  │    - "GENERATE_QUESTIONS" → Want questions?                  │  │
│  │    - null → Follow default flow                              │  │
│  │                                                              │  │
│  │ 2. Check execution flags in context:                         │  │
│  │    - backendDataProcessed?                                   │  │
│  │    - questionsGenerated?                                     │  │
│  │    - scriptGenerated?                                        │  │
│  │                                                              │  │
│  │ 3. Apply logic:                                              │  │
│  │    IF backend not done → RETURN Backend                      │  │
│  │    ELSE IF questions not done → RETURN Questions             │  │
│  │    ELSE IF script not done → RETURN Script                   │  │
│  │    ELSE → RETURN null (terminal)                             │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
│  RETURNS: ExecutionResult {                                          │
│    nextAgent: AgentType                                              │
│    contextDTO: updated context                                       │
│    reason: why this agent was chosen                                │
│    isTerminal: true/false                                            │
│  }                                                                    │
└──────────────────────┬───────────────────────────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────────────────────────┐
│  🤖 Orchestrator (THE EXECUTOR)                                      │
│  [AgentOrchestrationService]                                         │
│                                                                      │
│  EXECUTE:                                                            │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │ 1. Check result.isTerminal()                                 │  │
│  │    IF true → Return to controller                            │  │
│  │                                                              │  │
│  │ 2. Get agent from agentMap:                                  │  │
│  │    Agent agent = agentMap.get(result.getNextAgent())         │  │
│  │                                                              │  │
│  │ 3. Execute agent:                                            │  │
│  │    ContextDTO updated = agent.execute(context)              │  │
│  │                                                              │  │
│  │ 4. Track execution:                                          │  │
│  │    executionHistoryRepository.save(...)                      │  │
│  │                                                              │  │
│  │ 5. Update result with executed context:                      │  │
│  │    result.setContextDTO(updated)                             │  │
│  └──────────────────────────────────────────────────────────────┘  │
└──────────────────────┬───────────────────────────────────────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
    ┌─────────┐   ┌──────────┐   ┌────────┐
    │ Backend │   │Question  │   │ Script │
    │ Agent   │   │Bank Agent│   │Agent   │
    └────┬────┘   └─────┬────┘   └───┬────┘
         │              │            │
         │ execute()    │            │
         ├──────────────┼────────────┤
         │              │            │
    Input: Context     Input: Context     Input: Context
         │              │            │
    ├─ Build prompt ├─ Build prompt ├─ Build prompt
    ├─ Set flag    ├─ Set flag    ├─ Set flag
    └─ Return      └─ Return      └─ Return
                   updated context
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
┌──────────────────────────────────────────────────────────────────────┐
│           📊 Execution History Database                              │
│                                                                      │
│  Records:                                                            │
│  - Which agent executed                                              │
│  - When                                                              │
│  - What user action triggered it                                     │
│  - Execution order                                                   │
│                                                                      │
│  Used by Context Agent for future decisions!                        │
└──────────────────────────────────────────────────────────────────────┘
                       │
                       │ Updated ExecutionResult
                       ▼
┌──────────────────────────────────────────────────────────────────────┐
│                   Response to Frontend                               │
│                                                                      │
│  {                                                                   │
│    "nextAgent": "BACKEND" | "QUESTION_BANK" | "SCRIPT" | null,     │
│    "contextDTO": { updated context with prompts and flags },       │
│    "executionId": "uuid",                                          │
│    "reason": "Why this agent was chosen",                          │
│    "isTerminal": true | false,                                     │
│    "status": "SUCCESS"                                             │
│  }                                                                   │
└──────────────────────────────────────────────────────────────────────┘
```

---

## Decision Tree Flowchart

```
START: User sends request
│
├─ Context Agent receives:
│  - ContextDTO (current state)
│  - userAction (what user clicked)
│
▼
┌─────────────────────────────────────┐
│ Does user action exist?             │
└──────────────┬──────────────────────┘
               │
        ┌──────┴──────┐
        │             │
     YES│             │NO (null)
        ▼             │
    ┌──────────────┐  │ (Go to default flow)
    │ User action? │  │
    └──┬────┬──────┘  │
       │    │         │
       │    └─────────┘
  SCRIPT│   QUESTIONS
   or   │   or
 other  │   other
        │
        ▼
┌─────────────────────────────────────────┐
│ IF userAction == "GENERATE_SCRIPT":     │
│   Backend done?                         │
│   ├─ NO  → Return BACKEND              │
│   └─ YES → Return SCRIPT               │
│                                         │
│ ELSE IF userAction == "GENERATE_QUESTIONS":
│   Backend done?                         │
│   ├─ NO  → Return BACKEND              │
│   └─ YES → Return QUESTION_BANK        │
│                                         │
│ ELSE (No user action, default flow):   │
└────┬────────────────────────────────────┘
     │
     ▼
┌────────────────────────────────┐
│ Backend processed?             │
└────┬───────────────────────────┘
     │
  NO │                        YES
     │                          │
     ▼                          ▼
┌──────────────┐     ┌────────────────────────┐
│ Return:      │     │ Questions generated?   │
│ BACKEND      │     └────┬───────────────────┘
└──────────────┘          │
                      NO  │          YES
                          │            │
                          ▼            ▼
                    ┌──────────────┐ ┌──────────────┐
                    │ Return:      │ │ Script       │
                    │ QUESTION_BANK│ │ generated?   │
                    └──────────────┘ └────┬─────────┘
                                          │
                                      NO  │  YES
                                          │    │
                                          ▼    ▼
                                    ┌──────┐ ┌─────────┐
                                    │SCRIPT│ │null     │
                                    │      │ │TERMINAL │
                                    └──────┘ └─────────┘
```

---

## Complete Workflow Example

```
SCENARIO: New episode workflow

┌──────────────────────────────────────────────────┐
│ STEP 1: Frontend sends START request            │
│                                                 │
│ POST /api/orchestration/start                   │
│ {                                               │
│   "episodeId": 1,                               │
│   "episodeTitle": "AI in Healthcare",           │
│   "hostName": "Sarah",                          │
│   "guestName": "Dr. Andrew",                    │
│   "guestExpertise": "ML",                       │
│   "backendDataProcessed": false,                │
│   "questionsGenerated": false,                  │
│   "scriptGenerated": false                      │
│ }                                               │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 2: Context Agent ANALYZES                  │
│                                                 │
│ - userAction: null                              │
│ - backendDataProcessed: false                   │
│ - questionsGenerated: false                     │
│ - scriptGenerated: false                        │
│                                                 │
│ DECISION: Backend must run first!               │
│ RETURN: nextAgent = BACKEND                     │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 3: Orchestrator EXECUTES                   │
│                                                 │
│ GET Backend from agentMap                       │
│ CALL backend.execute(context)                   │
│                                                 │
│ Backend Agent:                                  │
│ ├─ Analyzes episode & guest                    │
│ ├─ Builds: "Process episode 'AI in...'"        │
│ ├─ Sets: backendDataProcessed = true           │
│ └─ Returns: updated context                    │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 4: Track Execution                         │
│                                                 │
│ INSERT INTO execution_history:                  │
│ - episodeId: 1                                  │
│ - executedAgent: BACKEND                       │
│ - userAction: null                              │
│ - executionOrder: 1                             │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 5: Response to Frontend                    │
│                                                 │
│ {                                               │
│   "nextAgent": "BACKEND",                       │
│   "contextDTO": {                               │
│     "episodeId": 1,                             │
│     "backendDataProcessed": true,  ← CHANGED   │
│     "questionBankPrompt": "Process...",         │
│     "questionsGenerated": false,                │
│     "scriptGenerated": false                    │
│   },                                            │
│   "isTerminal": false,                          │
│   "reason": "Backend executed"                  │
│ }                                               │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 6: User clicks "Generate Questions"       │
│                                                 │
│ POST /api/orchestration/generate-questions      │
│ {                                               │
│   "episodeId": 1,                               │
│   ... same context from previous response ...   │
│   "backendDataProcessed": true,                 │
│   "questionsGenerated": false                   │
│ }                                               │
│ userAction = "GENERATE_QUESTIONS"               │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 7: Context Agent ANALYZES (2nd cycle)     │
│                                                 │
│ - userAction: "GENERATE_QUESTIONS"              │
│ - backendDataProcessed: true                    │
│                                                 │
│ LOGIC:                                          │
│ IF userAction == "GENERATE_QUESTIONS":          │
│   Backend done? YES                             │
│   → RETURN: nextAgent = QUESTION_BANK           │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 8: Orchestrator EXECUTES Questions         │
│                                                 │
│ CALL questionBankAgent.execute(context)         │
│                                                 │
│ Question Agent:                                 │
│ ├─ Validates backend done                      │
│ ├─ Builds: "Generate 10 questions for..."      │
│ ├─ Sets: questionsGenerated = true             │
│ └─ Returns: updated context                    │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 9: Track Execution (2nd record)            │
│                                                 │
│ INSERT INTO execution_history:                  │
│ - episodeId: 1                                  │
│ - executedAgent: QUESTION_BANK                  │
│ - userAction: "GENERATE_QUESTIONS"              │
│ - executionOrder: 2                             │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 10: Response with Questions Done           │
│                                                 │
│ {                                               │
│   "nextAgent": "QUESTION_BANK",                 │
│   "contextDTO": {                               │
│     "episodeId": 1,                             │
│     "backendDataProcessed": true,               │
│     "questionsGenerated": true,  ← CHANGED     │
│     "questionBankPrompt": "Generate...",        │
│     "scriptGenerated": false                    │
│   },                                            │
│   "isTerminal": false                           │
│ }                                               │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 11: User clicks "Generate Script"         │
│                                                 │
│ POST /api/orchestration/generate-script         │
│ (Same context with all flags except script)     │
│ userAction = "GENERATE_SCRIPT"                  │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 12: Context Agent ANALYZES (3rd cycle)    │
│                                                 │
│ - userAction: "GENERATE_SCRIPT"                 │
│ - backendDataProcessed: true                    │
│ - questionsGenerated: true                      │
│                                                 │
│ DECISION: Generate script!                      │
│ RETURN: nextAgent = SCRIPT                      │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 13: Orchestrator EXECUTES Script           │
│                                                 │
│ CALL scriptAgent.execute(context)               │
│                                                 │
│ Script Agent:                                   │
│ ├─ Builds script prompt (includes questions)   │
│ ├─ Sets: scriptGenerated = true                │
│ └─ Returns: updated context                    │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 14: Track Execution (3rd record)           │
│                                                 │
│ INSERT INTO execution_history:                  │
│ - episodeId: 1                                  │
│ - executedAgent: SCRIPT                         │
│ - userAction: "GENERATE_SCRIPT"                 │
│ - executionOrder: 3                             │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 15: Response with Script Done              │
│                                                 │
│ {                                               │
│   "nextAgent": "SCRIPT",                        │
│   "contextDTO": {                               │
│     "episodeId": 1,                             │
│     "backendDataProcessed": true,               │
│     "questionsGenerated": true,                 │
│     "scriptGenerated": true,  ← CHANGED        │
│     "scriptPrompt": "Generate script..."        │
│   },                                            │
│   "isTerminal": false                           │
│ }                                               │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ STEP 16: All agents done, check status          │
│                                                 │
│ Context Agent ANALYZES:                         │
│ - All flags true                                │
│ - No more agents to run                         │
│                                                 │
│ RETURN: nextAgent = null, isTerminal = true    │
└──────┬───────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────┐
│ FINAL RESPONSE: Workflow Complete               │
│                                                 │
│ {                                               │
│   "nextAgent": null,                            │
│   "contextDTO": { all flags = true },           │
│   "isTerminal": true,  ← WORKFLOW DONE         │
│   "reason": "All agents executed"               │
│ }                                               │
└──────────────────────────────────────────────────┘
```

---

## Database State Throughout Workflow

```
EXECUTION_HISTORY Table at each stage:

AFTER Step 1-5 (Backend executed):
┌────────────────────────────────────────┐
│ Id │ Agent   │ Order │ UserAction      │
├────┼─────────┼───────┼─────────────────┤
│ 1  │ BACKEND │ 1     │ null            │
└────────────────────────────────────────┘

AFTER Step 6-9 (Questions executed):
┌────────────────────────────────────────┐
│ Id │ Agent    │ Order │ UserAction      │
├────┼──────────┼───────┼─────────────────┤
│ 1  │ BACKEND  │ 1     │ null            │
│ 2  │ QUESTION │ 2     │ GENERATE_QUESTIONS
└────────────────────────────────────────┘

AFTER Step 10-14 (Script executed):
┌────────────────────────────────────────┐
│ Id │ Agent   │ Order │ UserAction      │
├────┼─────────┼───────┼─────────────────┤
│ 1  │ BACKEND │ 1     │ null            │
│ 2  │ QUESTION│ 2     │ GENERATE_QUESTIONS
│ 3  │ SCRIPT  │ 3     │ GENERATE_SCRIPT │
└────────────────────────────────────────┘

This history is used by Context Agent for:
- Tracking what ran
- Making future decisions
- Audit trail
- Workflow analysis
```

---

## Agent Interaction Diagram

```
┌─────────────────────────────────────────────────────────┐
│                 CONTEXT AGENT                           │
│                                                         │
│  Gets: ContextDTO + userAction                          │
│  ├─ Analyzes state                                      │
│  ├─ Makes decision                                      │
│  └─ Returns: ExecutionResult                            │
│                                                         │
│  Does NOT execute agents                                │
│  Does NOT call database                                 │
│  Does NOT know agent details                            │
└────────────────────┬────────────────────────────────────┘
                     │ ExecutionResult
                     │ {nextAgent, context, reason}
                     ▼
┌─────────────────────────────────────────────────────────┐
│              ORCHESTRATOR                               │
│                                                         │
│  Gets: ExecutionResult from Context Agent              │
│  ├─ Extracts nextAgent                                  │
│  ├─ Gets agent from map                                 │
│  ├─ Calls agent.execute(context)                        │
│  ├─ Saves to database                                   │
│  └─ Returns: updated result                             │
│                                                         │
│  Executes agents                                        │
│  Tracks execution                                       │
│  Delegates work                                         │
└────────────────────┬────────────────────────────────────┘
                     │ agent.execute(context)
        ┌────────────┼────────────┐
        │            │            │
        ▼            ▼            ▼
    ┌────────┐  ┌────────┐  ┌────────┐
    │Backend │  │Question│  │Script  │
    │Agent   │  │Bank    │  │Agent   │
    │        │  │Agent   │  │        │
    │Gets:   │  │Gets:   │  │Gets:   │
    │Context │  │Context │  │Context │
    │        │  │        │  │        │
    │Action: │  │Action: │  │Action: │
    │Process │  │Generate│  │Generate│
    │& update│  │& update│  │& update│
    │        │  │        │  │        │
    │Returns:│  │Returns:│  │Returns:│
    │Updated │  │Updated │  │Updated │
    │Context │  │Context │  │Context │
    └────┬───┘  └────┬───┘  └────┬───┘
         │           │           │
         └───────────┼───────────┘
                     │ Updated ContextDTO
                     │ (with new flags & prompts)
                     ▼
         ┌──────────────────────────────┐
         │ Back to Orchestrator         │
         │ Save to database             │
         │ Return ExecutionResult       │
         └──────────────────────────────┘
```

---

These diagrams show the complete flow and interactions in your agent orchestration system!
