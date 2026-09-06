package com.example.model

enum class Tier(val label: String, val badge: String) {
    BEGINNER("Beginner", "T1"),
    INTERMEDIATE("Intermediate", "T2"),
    ADVANCED("Advanced", "T3")
}

enum class MemoryType {
    STACK,
    HEAP,
    STATIC_DATA
}

data class MemoryCell(
    val addressHex: String,
    val name: String,
    val typeName: String,
    val value: String,
    val sizeBytes: Int,
    val memoryType: MemoryType = MemoryType.STACK,
    val pointsToAddress: String? = null, // If this is a pointer, address it points to
    val isHighlighted: Boolean = false,
    val isFreed: Boolean = false,
    val isCorrupted: Boolean = false
)

data class StackFrame(
    val id: String,
    val functionName: String,
    val returnAddress: String,
    val parameters: List<String>,
    val localVariables: List<String>,
    val isActive: Boolean = true
)

data class CodeLine(
    val lineNumber: Int,
    val text: String,
    val explanation: String = "",
    val isBreakpoint: Boolean = false
)

enum class SimulationStatus {
    IDLE,
    RUNNING,
    STEPPING,
    PAUSED,
    COMPLETED,
    PANIC_ERROR
}

data class SimStep(
    val lineIndex: Int,
    val activeStatement: String,
    val statusText: String,
    val stdout: String? = null,
    val memorySnapshot: List<MemoryCell>,
    val stackSnapshot: List<StackFrame>,
    val activePointerSource: String? = null,
    val activePointerTarget: String? = null,
    val loopCounterInfo: Pair<String, Int>? = null,
    val isHazard: Boolean = false,
    val hazardMessage: String? = null
)

data class Level(
    val id: Int,
    val tier: Tier,
    val title: String,
    val subtitle: String,
    val conceptTitle: String,
    val conceptExplanation: String,
    val learningObjectives: List<String>,
    val sourceCode: List<CodeLine>,
    val initialMemory: List<MemoryCell>,
    val initialStack: List<StackFrame>,
    val steps: List<SimStep>,
    val challengeQuestion: String,
    val challengeOptions: List<String>,
    val correctOptionIndex: Int,
    val challengeHint: String,
    val cSnippetSnippet: String,
    val targetPointer: String = "0x7ffd",
    val defaultSize: String = "4 bytes"
)

enum class EventType {
    ANOMALY_FIX,
    MINI_PROJECT,
    DEBUG_CHALLENGE
}

data class InGameEvent(
    val id: String,
    val type: EventType,
    val title: String,
    val urgency: String, // e.g., "Critical", "Moderate", "Project"
    val description: String,
    val missionBrief: String,
    val codeSnippet: String,
    val question: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val rewardPoints: Int = 100,
    val isResolved: Boolean = false
)
