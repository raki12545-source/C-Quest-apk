package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.EventRepository
import com.example.data.LevelRepository
import com.example.model.SimulationStatus
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.CSimViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: CSimViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CSimApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CSimApp(viewModel: CSimViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLevelsDialog by remember { mutableStateOf(false) }
    var showEventsListDialog by remember { mutableStateOf(false) }
    var showCodexDialog by remember { mutableStateOf(false) }

    // Dialogs
    if (showLevelsDialog) {
        LevelSelectDialog(
            currentLevelId = uiState.currentLevel.id,
            unlockedLevelIds = uiState.unlockedLevelIds,
            completedLevelIds = uiState.completedLevelIds,
            onSelectLevel = { levelId ->
                viewModel.selectLevel(levelId)
                showLevelsDialog = false
            },
            onDismiss = { showLevelsDialog = false }
        )
    }

    if (showEventsListDialog) {
        EventsListDialog(
            resolvedEventIds = uiState.resolvedEventIds,
            onSelectEvent = { event ->
                showEventsListDialog = false
                viewModel.openEvent(event)
            },
            onDismiss = { showEventsListDialog = false }
        )
    }

    if (showCodexDialog) {
        CodexDialog(onDismiss = { showCodexDialog = false })
    }

    if (uiState.showAchievementsDialog) {
        AchievementsDialog(
            achievements = uiState.achievements,
            totalCredits = uiState.coreCredits,
            onDismiss = { viewModel.dismissAchievements() }
        )
    }

    // Event challenge modal
    if (uiState.selectedEvent != null) {
        EventSolveDialog(
            event = uiState.selectedEvent!!,
            selectedOption = uiState.selectedEventOption,
            result = uiState.eventResult,
            onSelectOption = { viewModel.selectEventOption(it) },
            onSubmit = { viewModel.submitEventAnswer() },
            onDismiss = { viewModel.closeEvent() }
        )
    }

    // End-of-level challenge modal
    if (uiState.showChallengeModal) {
        ChallengeCompleteDialog(
            level = uiState.currentLevel,
            selectedOption = uiState.selectedChallengeOption,
            result = uiState.challengeResult,
            onSelectOption = { viewModel.selectChallengeOption(it) },
            onSubmit = { viewModel.submitChallengeAnswer() },
            onNextLevel = {
                val nextId = uiState.currentLevel.id + 1
                if (nextId <= LevelRepository.levels.size) {
                    viewModel.selectLevel(nextId)
                }
                viewModel.dismissChallengeModal()
            },
            onDismiss = { viewModel.dismissChallengeModal() }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BentoBackground),
        containerColor = BentoBackground,
        topBar = {
            CSimTopBar(
                levelTitle = uiState.currentLevel.title,
                levelSubtitle = uiState.currentLevel.subtitle,
                credits = uiState.coreCredits,
                badgesCount = uiState.unlockedAchievementIds.size,
                totalBadgesCount = uiState.achievements.size,
                onOpenAchievements = { viewModel.openAchievements() },
                onOpenLevels = { showLevelsDialog = true },
                onOpenCodex = {
                    viewModel.openCodex()
                    showCodexDialog = true
                }
            )
        },
        bottomBar = {
            CSimBottomBar(
                status = uiState.simulationStatus,
                speedMultiplier = if (uiState.executionSpeedMs <= 600L) 2.0f else if (uiState.executionSpeedMs >= 2000L) 0.5f else 1.0f,
                onStep = { viewModel.stepForward() },
                onToggleRun = { viewModel.toggleRunSimulation() },
                onReset = { viewModel.resetSimulation() },
                onOpenLevels = { showLevelsDialog = true },
                onOpenEvents = { showEventsListDialog = true },
                onChangeSpeed = {
                    val nextSpeed = when {
                        uiState.executionSpeedMs > 1500L -> 1.0f
                        uiState.executionSpeedMs > 800L -> 2.0f
                        else -> 0.5f
                    }
                    viewModel.setSpeed(nextSpeed)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Animated Badge Unlock Banner
            BadgeUnlockBanner(
                achievement = uiState.recentlyUnlockedAchievement,
                onDismiss = { viewModel.dismissUnlockBanner() },
                onClick = {
                    viewModel.dismissUnlockBanner()
                    viewModel.openAchievements()
                }
            )

            // Bento Card 1: Execution Active Hero
            BentoHeroCard(
                title = uiState.activeStatementText.ifEmpty { uiState.currentLevel.conceptTitle },
                subtitle = uiState.statusMessage,
                status = uiState.simulationStatus,
                targetPointer = uiState.currentLevel.targetPointer,
                targetSize = uiState.currentLevel.defaultSize,
                stepFraction = "${uiState.currentStepIndex + 1}/${uiState.currentLevel.steps.size}",
                loopInfo = uiState.loopCounterInfo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            // Bento Grid Row 2: The Stack + Kernel Event / Hazard
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TheStackCard(
                    frames = uiState.stackFrames,
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxHeight()
                )

                val activeEvent = EventRepository.events.firstOrNull { !uiState.resolvedEventIds.contains(it.id) }
                    ?: EventRepository.events.first()

                EventBentoCard(
                    eventTitle = if (uiState.isHazardAlarmActive) "ANOMALY ALERT" else activeEvent.title,
                    eventSubtitle = if (uiState.isHazardAlarmActive) (uiState.hazardAlertText ?: "Segfault hazard imminent") else activeEvent.urgency,
                    isHazard = uiState.isHazardAlarmActive,
                    onClick = {
                        viewModel.openEvent(activeEvent)
                    },
                    modifier = Modifier
                        .weight(0.9f)
                        .fillMaxHeight()
                )
            }

            // Bento Milestones & Badges Quick Strip
            MilestonesBentoCard(
                achievements = uiState.achievements,
                onViewAll = { viewModel.openAchievements() }
            )

            // Bento Card 3: RAM Memory Inspector Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(BentoSurfaceVariant)
                    .border(1.dp, BentoBorderOutline, RoundedCornerShape(24.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "RAM MEMORY CELLS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoTextSecondary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "(${uiState.memoryCells.size} allocated)",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = BentoPrimary
                            )
                        }
                        if (uiState.activePointerSource != null && uiState.activePointerTarget != null) {
                            Text(
                                text = "${uiState.activePointerSource} -> ${uiState.activePointerTarget}",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = SynPointer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.memoryCells) { cell ->
                            val isPointerTarget = cell.addressHex == uiState.activePointerTarget
                            MemoryCellView(
                                cell = cell,
                                isPointerTarget = isPointerTarget,
                                modifier = Modifier.width(180.dp)
                            )
                        }
                    }
                }
            }

            // Bento Card 4: Dark C Console Terminal
            CodeTerminal(
                sourceCode = uiState.currentLevel.sourceCode,
                activeLineIndex = uiState.activeLineIndex,
                fileName = "main.c",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Bento Card 5: Stdout / Compiler Output
            StdoutBentoCard(
                stdoutText = uiState.stdoutLog,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun MilestonesBentoCard(
    achievements: List<com.example.model.Achievement>,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val unlockedCount = achievements.count { it.isUnlocked }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onViewAll)
            .testTag("milestones_bento_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BentoSurfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderOutline)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEF08A))
                            .border(1.dp, Color(0xFFEAB308), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFF854D0E),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "MILESTONES & BADGES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoTextSecondary,
                        letterSpacing = 0.5.sp
                    )

                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = BentoPrimaryContainer.copy(alpha = 0.7f)
                    ) {
                        Text(
                            text = "$unlockedCount/${achievements.size}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoPrimaryDark,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = "View All →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BentoPrimary
                )
            }

            // Horizontal badges preview row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(achievements) { ach ->
                    val tierBorderColor = Color(ach.badgeTier.borderColorHex)
                    val tierContainerColor = Color(ach.badgeTier.containerColorHex)

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (ach.isUnlocked) Color.White else Color.White.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (ach.isUnlocked) tierBorderColor else Color(0xFFCBD5E1)
                        ),
                        modifier = Modifier.clickable { onViewAll() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(if (ach.isUnlocked) tierContainerColor else Color(0xFFF1F5F9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getAchievementIcon(ach.iconKey),
                                    contentDescription = ach.title,
                                    tint = if (ach.isUnlocked) Color(ach.badgeTier.contentColorHex) else Color(0xFF94A3B8),
                                    modifier = Modifier.size(13.dp)
                                )
                            }

                            Text(
                                text = ach.title,
                                fontSize = 12.sp,
                                fontWeight = if (ach.isUnlocked) FontWeight.Bold else FontWeight.Medium,
                                color = if (ach.isUnlocked) BentoTextPrimary else BentoTextSecondary
                            )

                            Icon(
                                imageVector = if (ach.isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (ach.isUnlocked) Color(0xFF16A34A) else Color(0xFF94A3B8),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CSimTopBar(
    levelTitle: String,
    levelSubtitle: String,
    credits: Int,
    badgesCount: Int,
    totalBadgesCount: Int,
    onOpenAchievements: () -> Unit,
    onOpenLevels: () -> Unit,
    onOpenCodex: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .clickable(onClick = onOpenLevels)
                .testTag("header_level_badge")
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BentoPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = "Terminal Icon",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "C-Sim v2.0",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoTextPrimary
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(BentoPrimaryContainer)
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "$credits XP",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoPrimaryDark,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
                Text(
                    text = levelTitle,
                    fontSize = 11.5.sp,
                    color = BentoPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Trophies / Badges Button
            IconButton(
                onClick = onOpenAchievements,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFEF08A))
                    .border(1.dp, Color(0xFFEAB308), CircleShape)
                    .testTag("open_achievements_button")
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Milestones & Badges",
                    tint = Color(0xFF854D0E),
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = onOpenCodex,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BentoSurfaceVariant)
                    .testTag("open_codex_button")
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "Codex Reference",
                    tint = BentoTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = onOpenLevels,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BentoSurfaceVariant)
                    .testTag("open_levels_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Levels",
                    tint = BentoTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CSimBottomBar(
    status: SimulationStatus,
    speedMultiplier: Float,
    onStep: () -> Unit,
    onToggleRun: () -> Unit,
    onReset: () -> Unit,
    onOpenLevels: () -> Unit,
    onOpenEvents: () -> Unit,
    onChangeSpeed: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(100.dp))
                .background(BentoSurfaceVariant)
                .border(1.dp, BentoBorderOutline, RoundedCornerShape(100.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // STEP Button
            BentoNavButton(
                icon = Icons.Default.PlayCircle,
                label = "Step",
                isActive = true,
                onClick = onStep,
                tag = "bottom_step_button"
            )

            // RUN / PAUSE Button
            BentoNavButton(
                icon = if (status == SimulationStatus.RUNNING) Icons.Default.Pause else Icons.Default.PlayArrow,
                label = if (status == SimulationStatus.RUNNING) "Pause" else "Run",
                isActive = status == SimulationStatus.RUNNING,
                onClick = onToggleRun,
                tag = "bottom_run_button"
            )

            // SPEED Toggle Button
            BentoNavButton(
                icon = Icons.Default.Speed,
                label = "${speedMultiplier}x",
                isActive = false,
                onClick = onChangeSpeed,
                tag = "bottom_speed_button"
            )

            // RESET Button
            BentoNavButton(
                icon = Icons.Default.Replay,
                label = "Reset",
                isActive = false,
                onClick = onReset,
                tag = "bottom_reset_button"
            )

            // EVENTS Button
            BentoNavButton(
                icon = Icons.Default.Code,
                label = "Events",
                isActive = false,
                onClick = onOpenEvents,
                tag = "bottom_events_button"
            )

            // LEVELS Button
            BentoNavButton(
                icon = Icons.Default.FormatListNumbered,
                label = "Levels",
                isActive = false,
                onClick = onOpenLevels,
                tag = "bottom_levels_button"
            )
        }
    }
}

@Composable
private fun BentoNavButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(if (isActive) BentoPrimaryContainer else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag(tag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) BentoPrimaryDark else BentoTextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                color = if (isActive) BentoPrimaryDark else BentoTextSecondary
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
