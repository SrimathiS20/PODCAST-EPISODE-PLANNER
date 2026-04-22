/**
 * ORCHESTRATION API - EXAMPLE REQUESTS & RESPONSES
 * 
 * This file demonstrates how the API works and what responses to expect
 */

// ============================================================================
// 1. START WORKFLOW - Backend executes first
// ============================================================================

POST /api/orchestration/start
Content-Type: application/json

{
  "episodeId": 1,
  "episodeTitle": "The Future of AI",
  "hostName": "Sarah Johnson",
  "guestName": "Dr. Andrew Ng",
  "guestExpertise": "Deep Learning & Machine Learning",
  "guestBackground": "Co-founder of Coursera, AI researcher with 20+ years experience"
}

// RESPONSE (Backend Agent will execute next)
{
  "nextAgent": "BACKEND",
  "contextDTO": {
    "episodeId": 1,
    "episodeTitle": "The Future of AI",
    "hostName": "Sarah Johnson",
    "guestName": "Dr. Andrew Ng",
    "guestExpertise": "Deep Learning & Machine Learning",
    "guestBackground": "Co-founder of Coursera, AI researcher with 20+ years experience",
    "backendPrompt": "Process episode 'The Future of AI' with guest 'Dr. Andrew Ng' (Expertise: Deep Learning & Machine Learning). Prepare backend data including guest background: Co-founder of Coursera, AI researcher with 20+ years experience",
    "questionBankPrompt": null,
    "scriptPrompt": null,
    "additionalData": null,
    "backendDataProcessed": true,
    "questionsGenerated": false,
    "scriptGenerated": false
  },
  "executionId": "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6",
  "timestamp": "2024-04-22T10:30:45.123",
  "reason": "Execution state: [Backend=✅, Questions=❌, Script=❌]",
  "isTerminal": false,
  "status": "SUCCESS"
}

// ============================================================================
// 2. USER CLICKS "GENERATE QUESTIONS" 
// (Backend already done, so Question Bank will execute)
// ============================================================================

POST /api/orchestration/generate-questions
Content-Type: application/json

{
  "episodeId": 1,
  "episodeTitle": "The Future of AI",
  "hostName": "Sarah Johnson",
  "guestName": "Dr. Andrew Ng",
  "guestExpertise": "Deep Learning & Machine Learning",
  "guestBackground": "Co-founder of Coursera, AI researcher with 20+ years experience",
  "backendPrompt": "...",
  "backendDataProcessed": true,
  "questionsGenerated": false,
  "scriptGenerated": false
}

// RESPONSE (Question Bank Agent will execute next)
{
  "nextAgent": "QUESTION_BANK",
  "contextDTO": {
    "episodeId": 1,
    "episodeTitle": "The Future of AI",
    "hostName": "Sarah Johnson",
    "guestName": "Dr. Andrew Ng",
    "guestExpertise": "Deep Learning & Machine Learning",
    "backendDataProcessed": true,
    "questionsGenerated": true,
    "scriptGenerated": false,
    "questionBankPrompt": "Generate 10 insightful interview questions for 'Dr. Andrew Ng' who is a Deep Learning & Machine Learning expert. Episode: 'The Future of AI'. Host: 'Sarah Johnson'. Create questions that explore Deep Learning & Machine Learning expertise and insights."
  },
  "executionId": "b2c3d4e5-f6g7-h8i9-j0k1-l2m3n4o5p6q7",
  "timestamp": "2024-04-22T10:32:15.456",
  "reason": "Execution state: [Backend=✅, Questions=✅, Script=❌] | User action: GENERATE_QUESTIONS",
  "isTerminal": false,
  "status": "SUCCESS"
}

// ============================================================================
// 3. USER CLICKS "GENERATE SCRIPT"
// (Backend already done, skip to Script Agent)
// ============================================================================

POST /api/orchestration/generate-script
Content-Type: application/json

{
  "episodeId": 1,
  "episodeTitle": "The Future of AI",
  "hostName": "Sarah Johnson",
  "guestName": "Dr. Andrew Ng",
  "guestExpertise": "Deep Learning & Machine Learning",
  "backendDataProcessed": true,
  "questionsGenerated": true,
  "scriptGenerated": false
}

// RESPONSE (Script Agent will execute)
{
  "nextAgent": "SCRIPT",
  "contextDTO": {
    "episodeId": 1,
    "episodeTitle": "The Future of AI",
    "hostName": "Sarah Johnson",
    "guestName": "Dr. Andrew Ng",
    "guestExpertise": "Deep Learning & Machine Learning",
    "backendDataProcessed": true,
    "questionsGenerated": true,
    "scriptGenerated": true,
    "scriptPrompt": "Generate a complete podcast script for 'The Future of AI' episode. Host: 'Sarah Johnson', Guest: 'Dr. Andrew Ng' (Deep Learning & Machine Learning). Using the generated interview questions as reference. Create engaging introduction, interview flow, and conclusion. Include: opening remarks, guest introduction, interview Q&A flow, key insights, and closing."
  },
  "executionId": "c3d4e5f6-g7h8-i9j0-k1l2-m3n4o5p6q7r8",
  "timestamp": "2024-04-22T10:33:30.789",
  "reason": "Execution state: [Backend=✅, Questions=✅, Script=✅] | User action: GENERATE_SCRIPT",
  "isTerminal": false,
  "status": "SUCCESS"
}

// ============================================================================
// 4. ALL AGENTS EXECUTED - WORKFLOW COMPLETE
// (Next call will show terminal=true)
// ============================================================================

// RESPONSE (No more agents to execute)
{
  "nextAgent": null,
  "contextDTO": {
    "episodeId": 1,
    "episodeTitle": "The Future of AI",
    "hostName": "Sarah Johnson",
    "guestName": "Dr. Andrew Ng",
    "guestExpertise": "Deep Learning & Machine Learning",
    "backendDataProcessed": true,
    "questionsGenerated": true,
    "scriptGenerated": true
  },
  "executionId": "d4e5f6g7-h8i9-j0k1-l2m3-n4o5p6q7r8s9",
  "timestamp": "2024-04-22T10:34:45.012",
  "reason": "All agents executed. Workflow complete.",
  "isTerminal": true,
  "status": "SUCCESS"
}

// ============================================================================
// 5. RUN ALL AGENTS CONTINUOUSLY
// (Executes all remaining agents in sequence)
// ============================================================================

POST /api/orchestration/run-all
Content-Type: application/json

{
  "episodeId": 2,
  "episodeTitle": "Quantum Computing Basics",
  "hostName": "Michael Chen",
  "guestName": "Prof. Qiming Sun",
  "guestExpertise": "Quantum Computing",
  "guestBackground": "PhD in Physics, 15 years quantum research"
}

// RESPONSE (All agents executed, workflow complete)
{
  "nextAgent": null,
  "contextDTO": {
    "episodeId": 2,
    "episodeTitle": "Quantum Computing Basics",
    "hostName": "Michael Chen",
    "guestName": "Prof. Qiming Sun",
    "guestExpertise": "Quantum Computing",
    "guestBackground": "PhD in Physics, 15 years quantum research",
    "backendPrompt": "Process episode 'Quantum Computing Basics' with guest 'Prof. Qiming Sun' ...",
    "questionBankPrompt": "Generate 10 insightful interview questions for 'Prof. Qiming Sun' who is a Quantum Computing expert...",
    "scriptPrompt": "Generate a complete podcast script for 'Quantum Computing Basics' episode...",
    "backendDataProcessed": true,
    "questionsGenerated": true,
    "scriptGenerated": true
  },
  "executionId": "e5f6g7h8-i9j0-k1l2-m3n4-o5p6q7r8s9t0",
  "timestamp": "2024-04-22T10:36:00.345",
  "reason": "Workflow complete. All agents have executed.",
  "isTerminal": true,
  "status": "SUCCESS"
}

// ============================================================================
// EXECUTION HISTORY IN DATABASE
// ============================================================================

// ExecutionHistory records for episode 1:

Record 1:
{
  "id": 1,
  "episodeId": 1,
  "executedAgent": "BACKEND",
  "executionId": "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6",
  "executedAt": "2024-04-22T10:30:45",
  "status": "SUCCESS",
  "userAction": null,
  "executionOrder": 1
}

Record 2:
{
  "id": 2,
  "episodeId": 1,
  "executedAgent": "QUESTION_BANK",
  "executionId": "b2c3d4e5-f6g7-h8i9-j0k1-l2m3n4o5p6q7",
  "executedAt": "2024-04-22T10:32:15",
  "status": "SUCCESS",
  "userAction": "GENERATE_QUESTIONS",
  "executionOrder": 2
}

Record 3:
{
  "id": 3,
  "episodeId": 1,
  "executedAgent": "SCRIPT",
  "executionId": "c3d4e5f6-g7h8-i9j0-k1l2-m3n4o5p6q7r8",
  "executedAt": "2024-04-22T10:33:30",
  "status": "SUCCESS",
  "userAction": "GENERATE_SCRIPT",
  "executionOrder": 3
}

// ============================================================================
// DECISION LOGIC FLOW
// ============================================================================

SCENARIO: User wants to skip questions and go straight to script

1. User clicks "Generate Script"
   userAction = "GENERATE_SCRIPT"

2. Context Agent analyzes:
   - Is Backend done? YES
   - Do we need Backend? NO (already done)
   - User wants Script? YES
   → Decision: Execute SCRIPT Agent
   → Skip Question Bank Agent entirely

3. Orchestrator executes Script Agent
   - Script Agent doesn't care about questions
   - Generates script based on backend data only
   - Returns updated context

4. Result: Script generated without generating questions
   - Efficient workflow
   - Respects user choice
   - Maintains data integrity

// ============================================================================
// ERROR SCENARIO
// ============================================================================

SCENARIO: User tries to generate questions before backend

1. User somehow sends:
{
  "episodeId": 1,
  "backendDataProcessed": false,
  "questionsGenerated": false
}
userAction = "GENERATE_QUESTIONS"

2. Context Agent analyzes:
   - User wants Questions? YES
   - Is Backend done? NO
   → Decision: Execute BACKEND Agent first!
   → Then Questions can execute next cycle

3. Ensures proper dependency order:
   Backend ← prerequisite for Questions
   → Clean error handling through decision logic

// ============================================================================
// KEY INSIGHTS
// ============================================================================

✅ Context Agent DECIDES based on:
   - Execution history flags (backendDataProcessed, questionsGenerated, etc.)
   - User interaction (userAction parameter)

✅ Orchestrator EXECUTES:
   - Gets decision from Context Agent
   - Finds agent from map
   - Calls agent.execute(context)
   - Tracks in database

✅ Agents are PURE:
   - Take context, return updated context
   - Don't know about other agents
   - Don't make decisions
   - Just execute their task

✅ System is FLEXIBLE:
   - User can generate in any order (with dependencies respected)
   - Easy to add new agents
   - Easy to modify decision logic
   - Decisions are centralized in Context Agent
