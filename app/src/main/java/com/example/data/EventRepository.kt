package com.example.data

import com.example.model.EventType
import com.example.model.InGameEvent

object EventRepository {

    val events: List<InGameEvent> = listOf(
        InGameEvent(
            id = "evt_traffic_light",
            type = EventType.MINI_PROJECT,
            title = "Traffic Light Controller",
            urgency = "Embedded Project",
            description = "Simulate an embedded intersection state machine using C enums and switch statements.",
            missionBrief = "An automated city intersection controller is dropping state transitions. Construct the C logic to transition the light safely: RED -> GREEN -> YELLOW -> RED.",
            codeSnippet = """
typedef enum { STATE_RED, STATE_GREEN, STATE_YELLOW } LightState;

LightState next_state(LightState current) {
    switch (current) {
        case STATE_RED:    return STATE_GREEN;
        case STATE_GREEN:  return STATE_YELLOW;
        case STATE_YELLOW: /* MISSING TRANSITION */;
        default:           return STATE_RED;
    }
}
            """.trimIndent(),
            question = "What should the case STATE_YELLOW return to complete the traffic cycle?",
            choices = listOf(
                "return STATE_RED;",
                "return STATE_GREEN;",
                "return STATE_YELLOW;",
                "break;"
            ),
            correctIndex = 0,
            explanation = "After the yellow warning phase, the traffic light cycles back to STATE_RED to stop cross-traffic.",
            rewardPoints = 150
        ),

        InGameEvent(
            id = "evt_calculator",
            type = EventType.MINI_PROJECT,
            title = "Simple Calculator Engine",
            urgency = "CLI Utility",
            description = "Build a C arithmetic engine supporting +, -, *, and safe integer division / with zero-check.",
            missionBrief = "Implement evaluate(int a, char op, int b). If op == '/' and b == 0, the program must return an error code rather than triggering a hardware division-by-zero trap.",
            codeSnippet = """
int evaluate(int a, char op, int b, int *error) {
    *error = 0;
    switch(op) {
        case '+': return a + b;
        case '-': return a - b;
        case '*': return a * b;
        case '/':
            if (/* MISSING GUARD */) {
                *error = 1; // Division by zero!
                return 0;
            }
            return a / b;
    }
    return 0;
}
            """.trimIndent(),
            question = "Which guard condition properly prevents a fatal division-by-zero in C?",
            choices = listOf(
                "b == 0",
                "a == 0",
                "b != 0",
                "a / b == 0"
            ),
            correctIndex = 0,
            explanation = "Dividing by zero causes an undefined behavior / hardware trap in C. Checking 'b == 0' protects the CPU.",
            rewardPoints = 200
        ),

        InGameEvent(
            id = "evt_mem_leak",
            type = EventType.ANOMALY_FIX,
            title = "CRITICAL: Memory Leak Emergency",
            urgency = "Hazard Alert",
            description = "Heap memory usage is climbing rapidly. A network buffer is allocated on every packet without release.",
            missionBrief = "The process packet_handler() allocates 1024 bytes per message but exits early on error without freeing the buffer. Locate the fix.",
            codeSnippet = """
void process_packet(Packet *p) {
    char *buf = (char*)malloc(1024);
    if (!verify_checksum(p)) {
        /* LEAK DETECTED HERE */
        return; 
    }
    dispatch(buf);
    free(buf);
}
            """.trimIndent(),
            question = "How must you fix the checksum failure exit to avert a catastrophic memory leak?",
            choices = listOf(
                "free(buf); before return;",
                "buf = NULL; before return;",
                "realloc(buf, 0);",
                "Do nothing, C cleans it up"
            ),
            correctIndex = 0,
            explanation = "If you return without calling free(buf), the address in buf is lost on the Stack, leaving 1024 bytes permanently orphaned on the Heap.",
            rewardPoints = 250
        ),

        InGameEvent(
            id = "evt_segfault",
            type = EventType.ANOMALY_FIX,
            title = "Kernel Panic: SIGSEGV Anomaly",
            urgency = "Fault Resolution",
            description = "A null pointer dereference triggered an unhandled Segmentation Fault (Exit Code 139).",
            missionBrief = "The kernel crashed when attempting to write data through a pointer that failed its lookup and remained NULL.",
            codeSnippet = """
void update_player_score(Player *player, int score) {
    /* CORRECTION NEEDED */
    player->score = score;
    printf("Updated score: %d\n", player->score);
}
            """.trimIndent(),
            question = "What safeguard line must precede player->score access to prevent SIGSEGV?",
            choices = listOf(
                "if (player == NULL) return;",
                "if (&player != NULL) return;",
                "player = malloc(sizeof(Player));",
                "assert(player == 0);"
            ),
            correctIndex = 0,
            explanation = "Checking 'if (player == NULL) return;' ensures the function never attempts to dereference address 0x00, preventing segmentation faults.",
            rewardPoints = 200
        ),

        InGameEvent(
            id = "evt_buffer_overflow",
            type = EventType.DEBUG_CHALLENGE,
            title = "Buffer Overflow Incident",
            urgency = "Security Vulnerability",
            description = "An unchecked loop writes beyond the declared bounds of an array, corrupting adjacent stack variables.",
            missionBrief = "An array of 4 ints is populated by a loop with an off-by-one condition 'i <= 4', overwriting the adjacent 'isAdmin' authentication flag!",
            codeSnippet = """
int isAdmin = 0; // Stack address: 0x7ffd1010
int buffer[4];   // Stack address: 0x7ffd1000 to 0x7ffd100C

// VULNERABILITY:
for (int i = 0; i <= 4; i++) {
    buffer[i] = 1; // i=4 writes directly into isAdmin!
}
            """.trimIndent(),
            question = "What is the correct loop termination condition to prevent writing past the array bounds?",
            choices = listOf(
                "i < 4",
                "i <= 4",
                "i < 5",
                "i == 4"
            ),
            correctIndex = 0,
            explanation = "For an array of size 4, valid indices are strictly 0, 1, 2, and 3. The loop condition must be 'i < 4'.",
            rewardPoints = 220
        )
    )
}
