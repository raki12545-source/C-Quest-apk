package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AchievementRepository
import com.example.data.EventRepository
import com.example.data.LevelRepository
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppScreen {
    BENTO_SIMULATION,
    LEVEL_SELECTOR,
    EVENTS_CENTER,
    MEMORY_INSPECTOR,
    CODEX_REFERENCE,
    ACHIEVEMENTS
}

data class SimulationUiState(
    val currentLevel: Level = LevelRepository.levels[0],
    val currentStepIndex: Int = 0,
    val simulationStatus: SimulationStatus = SimulationStatus.IDLE,
    val executionSpeedMs: Long = 1200L,
    val memoryCells: List<MemoryCell> = LevelRepository.levels[0].initialMemory,
    val stackFrames: List<StackFrame> = LevelRepository.levels[0].initialStack,
    val stdoutLog: String = "gcc -Wall -Werror main.c -o c_sim\n[Virtual C Kernel Ready]\n",
    val activeLineIndex: Int = 0,
    val activeStatementText: String = "",
    val statusMessage: String = "Ready to step or run simulation",
    val unlockedLevelIds: Set<Int> = setOf(1),
    val completedLevelIds: Set<Int> = emptySet(),
    val levelScores: Map<Int, Int> = emptyMap(),
    val coreCredits: Int = 100,
    val activeScreen: AppScreen = AppScreen.BENTO_SIMULATION,
    val selectedEvent: InGameEvent? = null,
    val resolvedEventIds: Set<String> = emptySet(),
    val showChallengeModal: Boolean = false,
    val selectedChallengeOption: Int? = null,
    val challengeResult: Boolean? = null,
    val selectedEventOption: Int? = null,
    val eventResult: Boolean? = null,
    val activePointerSource: String? = null,
    val activePointerTarget: String? = null,
    val loopCounterInfo: Pair<String, Int>? = null,
    val isHazardAlarmActive: Boolean = false,
    val hazardAlertText: String? = null,
    val showVictoryCelebration: Boolean = false,
    val achievements: List<Achievement> = AchievementRepository.allAchievements,
    val unlockedAchievementIds: Set<String> = emptySet(),
    val recentlyUnlockedAchievement: Achievement? = null,
    val showAchievementsDialog: Boolean = false,
    val hasExecutedStep: Boolean = false,
    val hasOpenedCodex: Boolean = false,
    val hasUsedHighSpeed: Boolean = false
)

class CSimViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("c_sim_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(loadInitialState())
    val uiState: StateFlow<SimulationUiState> = _uiState.asStateFlow()

    private var runJob: Job? = null

    private fun loadInitialState(): SimulationUiState {
        val unlockedSaved = prefs.getStringSet("unlocked_levels", setOf("1"))?.mapNotNull { it.toIntOrNull() }?.toSet() ?: setOf(1)
        val completedSaved = prefs.getStringSet("completed_levels", emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        val creditsSaved = prefs.getInt("core_credits", 100)
        val resolvedEvents = prefs.getStringSet("resolved_events", emptySet()) ?: emptySet()
        val unlockedAchSaved = prefs.getStringSet("unlocked_achievements", emptySet()) ?: emptySet()
        val hasStepSaved = prefs.getBoolean("has_executed_step", false)
        val hasCodexSaved = prefs.getBoolean("has_opened_codex", false)
        val hasSpeedSaved = prefs.getBoolean("has_used_high_speed", false)

        val computedAchievements = AchievementRepository.computeAchievements(
            unlockedAchievementIds = unlockedAchSaved,
            completedLevelIds = completedSaved,
            hasExecutedStep = hasStepSaved,
            hasOpenedCodex = hasCodexSaved,
            hasUsedHighSpeed = hasSpeedSaved,
            resolvedEventIds = resolvedEvents
        )
        val allUnlockedNow = computedAchievements.filter { it.isUnlocked }.map { it.id }.toSet()

        val firstLevel = LevelRepository.levels[0]
        return SimulationUiState(
            currentLevel = firstLevel,
            currentStepIndex = 0,
            unlockedLevelIds = unlockedSaved,
            completedLevelIds = completedSaved,
            coreCredits = creditsSaved,
            resolvedEventIds = resolvedEvents,
            achievements = computedAchievements,
            unlockedAchievementIds = allUnlockedNow,
            hasExecutedStep = hasStepSaved,
            hasOpenedCodex = hasCodexSaved,
            hasUsedHighSpeed = hasSpeedSaved,
            memoryCells = firstLevel.initialMemory,
            stackFrames = firstLevel.initialStack,
            activeStatementText = firstLevel.sourceCode.firstOrNull()?.text ?: ""
        )
    }

    private fun saveProgress() {
        val state = _uiState.value
        prefs.edit()
            .putStringSet("unlocked_levels", state.unlockedLevelIds.map { it.toString() }.toSet())
            .putStringSet("completed_levels", state.completedLevelIds.map { it.toString() }.toSet())
            .putInt("core_credits", state.coreCredits)
            .putStringSet("resolved_events", state.resolvedEventIds)
            .putStringSet("unlocked_achievements", state.unlockedAchievementIds)
            .putBoolean("has_executed_step", state.hasExecutedStep)
            .putBoolean("has_opened_codex", state.hasOpenedCodex)
            .putBoolean("has_used_high_speed", state.hasUsedHighSpeed)
            .apply()
    }

    fun selectLevel(levelId: Int) {
        val level = LevelRepository.levels.find { it.id == levelId } ?: return
        if (!_uiState.value.unlockedLevelIds.contains(levelId)) return

        pauseSimulation()
        _uiState.update { current ->
            current.copy(
                currentLevel = level,
                currentStepIndex = 0,
                simulationStatus = SimulationStatus.IDLE,
                memoryCells = level.initialMemory,
                stackFrames = level.initialStack,
                activeLineIndex = 0,
                activeStatementText = level.sourceCode.firstOrNull()?.text ?: "",
                statusMessage = "Loaded ${level.title}. Press Step or Run.",
                stdoutLog = "=== Compiling ${level.title} ===\ngcc -O2 main.c -o sim\n[Build Successful]\n",
                activePointerSource = null,
                activePointerTarget = null,
                loopCounterInfo = null,
                isHazardAlarmActive = false,
                hazardAlertText = null,
                showVictoryCelebration = false,
                activeScreen = AppScreen.BENTO_SIMULATION,
                showChallengeModal = false,
                selectedChallengeOption = null,
                challengeResult = null
            )
        }
    }

    fun stepForward() {
        val state = _uiState.value
        val level = state.currentLevel
        val nextIndex = state.currentStepIndex + 1

        if (nextIndex < level.steps.size) {
            val step = level.steps[nextIndex]
            val newStdout = if (step.stdout != null) state.stdoutLog + step.stdout else state.stdoutLog

            _uiState.update { current ->
                current.copy(
                    currentStepIndex = nextIndex,
                    simulationStatus = SimulationStatus.STEPPING,
                    memoryCells = step.memorySnapshot,
                    stackFrames = step.stackSnapshot,
                    activeLineIndex = step.lineIndex,
                    activeStatementText = step.activeStatement,
                    statusMessage = step.statusText,
                    stdoutLog = newStdout,
                    activePointerSource = step.activePointerSource,
                    activePointerTarget = step.activePointerTarget,
                    loopCounterInfo = step.loopCounterInfo,
                    isHazardAlarmActive = step.isHazard,
                    hazardAlertText = step.hazardMessage,
                    hasExecutedStep = true
                )
            }
            checkAchievements(markStepped = true)
        } else {
            // Reached end of steps
            onLevelSimulationFinished()
        }
    }

    fun toggleRunSimulation() {
        if (_uiState.value.simulationStatus == SimulationStatus.RUNNING) {
            pauseSimulation()
        } else {
            startContinuousRun()
        }
    }

    private fun startContinuousRun() {
        runJob?.cancel()
        _uiState.update { it.copy(simulationStatus = SimulationStatus.RUNNING) }

        runJob = viewModelScope.launch {
            while (_uiState.value.simulationStatus == SimulationStatus.RUNNING) {
                delay(_uiState.value.executionSpeedMs)
                val state = _uiState.value
                val level = state.currentLevel
                if (state.currentStepIndex + 1 < level.steps.size) {
                    stepForward()
                } else {
                    onLevelSimulationFinished()
                    break
                }
            }
        }
    }

    fun pauseSimulation() {
        runJob?.cancel()
        runJob = null
        if (_uiState.value.simulationStatus == SimulationStatus.RUNNING) {
            _uiState.update { it.copy(simulationStatus = SimulationStatus.PAUSED) }
        }
    }

    fun resetSimulation() {
        pauseSimulation()
        val level = _uiState.value.currentLevel
        _uiState.update { current ->
            current.copy(
                currentStepIndex = 0,
                simulationStatus = SimulationStatus.IDLE,
                memoryCells = level.initialMemory,
                stackFrames = level.initialStack,
                activeLineIndex = 0,
                activeStatementText = level.sourceCode.firstOrNull()?.text ?: "",
                statusMessage = "Simulation reset to start of ${level.title}.",
                stdoutLog = "[Reset execution pointer to main()]\n",
                activePointerSource = null,
                activePointerTarget = null,
                loopCounterInfo = null,
                isHazardAlarmActive = false,
                hazardAlertText = null,
                showVictoryCelebration = false,
                showChallengeModal = false,
                selectedChallengeOption = null,
                challengeResult = null
            )
        }
    }

    private fun onLevelSimulationFinished() {
        pauseSimulation()
        _uiState.update { current ->
            current.copy(
                simulationStatus = SimulationStatus.COMPLETED,
                statusMessage = "Simulation Complete! Test your understanding.",
                showChallengeModal = true,
                stdoutLog = current.stdoutLog + "\n[Execution finished. Ready for Challenge Assessment]\n"
            )
        }
    }

    fun selectChallengeOption(index: Int) {
        _uiState.update { it.copy(selectedChallengeOption = index) }
    }

    fun submitChallengeAnswer() {
        val state = _uiState.value
        val selected = state.selectedChallengeOption ?: return
        val isCorrect = selected == state.currentLevel.correctOptionIndex

        val currentLevelId = state.currentLevel.id
        val nextLevelId = currentLevelId + 1

        val updatedUnlocked = if (isCorrect && nextLevelId <= LevelRepository.levels.size) {
            state.unlockedLevelIds + nextLevelId
        } else {
            state.unlockedLevelIds
        }

        val updatedCompleted = if (isCorrect) {
            state.completedLevelIds + currentLevelId
        } else {
            state.completedLevelIds
        }

        val bonusCredits = if (isCorrect) 100 else 10
        val updatedCredits = state.coreCredits + bonusCredits

        _uiState.update { current ->
            current.copy(
                challengeResult = isCorrect,
                unlockedLevelIds = updatedUnlocked,
                completedLevelIds = updatedCompleted,
                coreCredits = updatedCredits,
                showVictoryCelebration = isCorrect,
                statusMessage = if (isCorrect) "Challenge Passed! +$bonusCredits Credits" else "Incorrect answer. Review the concept and retry."
            )
        }
        saveProgress()
        if (isCorrect) {
            checkAchievements()
        }
    }

    fun dismissChallengeModal() {
        _uiState.update { it.copy(showChallengeModal = false) }
    }

    fun setSpeed(speedMultiplier: Float) {
        val newSpeed = when (speedMultiplier) {
            0.5f -> 2000L
            1.0f -> 1200L
            2.0f -> 600L
            else -> 1200L
        }
        val isHighSpeed = speedMultiplier >= 2.0f
        _uiState.update { it.copy(executionSpeedMs = newSpeed, hasUsedHighSpeed = it.hasUsedHighSpeed || isHighSpeed) }
        if (isHighSpeed) {
            checkAchievements(markHighSpeed = true)
        }
    }

    fun openEvent(event: InGameEvent) {
        _uiState.update {
            it.copy(
                selectedEvent = event,
                selectedEventOption = null,
                eventResult = null
            )
        }
    }

    fun closeEvent() {
        _uiState.update {
            it.copy(
                selectedEvent = null,
                selectedEventOption = null,
                eventResult = null
            )
        }
    }

    fun selectEventOption(index: Int) {
        _uiState.update { it.copy(selectedEventOption = index) }
    }

    fun submitEventAnswer() {
        val state = _uiState.value
        val event = state.selectedEvent ?: return
        val selected = state.selectedEventOption ?: return

        val isCorrect = selected == event.correctIndex
        val updatedResolved = if (isCorrect) state.resolvedEventIds + event.id else state.resolvedEventIds
        val updatedCredits = if (isCorrect) state.coreCredits + event.rewardPoints else state.coreCredits

        _uiState.update { current ->
            current.copy(
                eventResult = isCorrect,
                resolvedEventIds = updatedResolved,
                coreCredits = updatedCredits
            )
        }
        saveProgress()
        if (isCorrect) {
            checkAchievements()
        }
    }

    fun openCodex() {
        _uiState.update { it.copy(hasOpenedCodex = true) }
        checkAchievements(markCodexOpened = true)
    }

    fun openAchievements() {
        _uiState.update { it.copy(showAchievementsDialog = true) }
    }

    fun dismissAchievements() {
        _uiState.update { it.copy(showAchievementsDialog = false) }
    }

    fun dismissUnlockBanner() {
        _uiState.update { it.copy(recentlyUnlockedAchievement = null) }
    }

    fun checkAchievements(
        markStepped: Boolean = false,
        markCodexOpened: Boolean = false,
        markHighSpeed: Boolean = false
    ) {
        val currentState = _uiState.value
        val hasStep = currentState.hasExecutedStep || markStepped
        val hasCodex = currentState.hasOpenedCodex || markCodexOpened
        val hasSpeed = currentState.hasUsedHighSpeed || markHighSpeed

        val evaluatedAchievements = AchievementRepository.computeAchievements(
            unlockedAchievementIds = currentState.unlockedAchievementIds,
            completedLevelIds = currentState.completedLevelIds,
            hasExecutedStep = hasStep,
            hasOpenedCodex = hasCodex,
            hasUsedHighSpeed = hasSpeed,
            resolvedEventIds = currentState.resolvedEventIds
        )

        val newlyUnlocked = evaluatedAchievements.filter { it.isUnlocked && !currentState.unlockedAchievementIds.contains(it.id) }

        if (newlyUnlocked.isNotEmpty()) {
            val totalBonus = newlyUnlocked.sumOf { it.rewardCredits }
            val updatedUnlockedIds = currentState.unlockedAchievementIds + newlyUnlocked.map { it.id }.toSet()
            val latestBadge = newlyUnlocked.last()

            _uiState.update { current ->
                current.copy(
                    achievements = evaluatedAchievements,
                    unlockedAchievementIds = updatedUnlockedIds,
                    coreCredits = current.coreCredits + totalBonus,
                    recentlyUnlockedAchievement = latestBadge,
                    hasExecutedStep = hasStep,
                    hasOpenedCodex = hasCodex,
                    hasUsedHighSpeed = hasSpeed
                )
            }
            saveProgress()

            // Auto-dismiss banner after 4.5 seconds
            viewModelScope.launch {
                delay(4500)
                if (_uiState.value.recentlyUnlockedAchievement?.id == latestBadge.id) {
                    _uiState.update { it.copy(recentlyUnlockedAchievement = null) }
                }
            }
        } else {
            _uiState.update { current ->
                current.copy(
                    achievements = evaluatedAchievements,
                    hasExecutedStep = hasStep,
                    hasOpenedCodex = hasCodex,
                    hasUsedHighSpeed = hasSpeed
                )
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(activeScreen = screen) }
    }

    fun dismissCelebration() {
        _uiState.update { it.copy(showVictoryCelebration = false) }
    }
}
