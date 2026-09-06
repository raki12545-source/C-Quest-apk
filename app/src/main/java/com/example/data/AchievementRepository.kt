package com.example.data

import com.example.model.Achievement
import com.example.model.BadgeTier

object AchievementRepository {

    val allAchievements: List<Achievement> = listOf(
        Achievement(
            id = "ach_pointer_pro",
            title = "Pointer Pro",
            subtitle = "Direct Address Navigation",
            description = "Master pointer dereferencing (*ptr) & address referencing (&var) in Level 6 or Level 7.",
            badgeTier = BadgeTier.GOLD,
            category = "Pointers",
            iconKey = "pointer_pro",
            rewardCredits = 250,
            maxProgress = 1
        ),
        Achievement(
            id = "ach_memory_master",
            title = "Memory Master",
            subtitle = "Heap & Leak Safeguard",
            description = "Safely manage dynamic heap buffers (malloc/free) in Level 8 or neutralize the critical Memory Leak anomaly.",
            badgeTier = BadgeTier.DIAMOND,
            category = "Memory",
            iconKey = "memory_master",
            rewardCredits = 300,
            maxProgress = 1
        ),
        Achievement(
            id = "ach_first_compile",
            title = "First Compile",
            subtitle = "Genesis Instruction",
            description = "Step through your first ANSI C machine instruction in the kernel simulator.",
            badgeTier = BadgeTier.BRONZE,
            category = "Core",
            iconKey = "compile",
            rewardCredits = 50,
            maxProgress = 1
        ),
        Achievement(
            id = "ach_stack_commander",
            title = "Stack Commander",
            subtitle = "LIFO Frame Isolation",
            description = "Navigate nested function call frames and return addresses in Level 5.",
            badgeTier = BadgeTier.SILVER,
            category = "Core",
            iconKey = "stack",
            rewardCredits = 150,
            maxProgress = 1
        ),
        Achievement(
            id = "ach_loop_wizard",
            title = "Loop Wizard",
            subtitle = "Register Incrementer",
            description = "Successfully iterate loops and watch index registers advance in Level 4.",
            badgeTier = BadgeTier.SILVER,
            category = "Core",
            iconKey = "loop",
            rewardCredits = 150,
            maxProgress = 1
        ),
        Achievement(
            id = "ach_bug_hunter",
            title = "Bug Hunter",
            subtitle = "Kernel Anomaly Resolver",
            description = "Diagnose and patch at least one critical vulnerability (SIGSEGV, Overflow, or Memory Leak).",
            badgeTier = BadgeTier.GOLD,
            category = "Events",
            iconKey = "bug",
            rewardCredits = 200,
            maxProgress = 1
        ),
        Achievement(
            id = "ach_struct_specialist",
            title = "Struct Specialist",
            subtitle = "Composite Data Architect",
            description = "Organize contiguous memory structures and navigate the arrow dereference operator (->) in Level 9.",
            badgeTier = BadgeTier.GOLD,
            category = "Memory",
            iconKey = "struct",
            rewardCredits = 250,
            maxProgress = 1
        ),
        Achievement(
            id = "ach_overclocked",
            title = "Overclocked",
            subtitle = "High-Frequency Stepping",
            description = "Accelerate simulator clock speed to 2.0x frequency.",
            badgeTier = BadgeTier.BRONZE,
            category = "Core",
            iconKey = "speed",
            rewardCredits = 75,
            maxProgress = 1
        ),
        Achievement(
            id = "ach_codex_scholar",
            title = "Codex Scholar",
            subtitle = "Architectural Knowledge",
            description = "Open and consult the ANSI C Specification and Virtual Memory Codex.",
            badgeTier = BadgeTier.BRONZE,
            category = "Core",
            iconKey = "codex",
            rewardCredits = 50,
            maxProgress = 1
        ),
        Achievement(
            id = "ach_kernel_legend",
            title = "Kernel Legend",
            subtitle = "Full Curriculum Grandmaster",
            description = "Master all 9 core simulation levels spanning Primitives, Control Flow, Pointers, and Heap.",
            badgeTier = BadgeTier.LEGENDARY,
            category = "Mastery",
            iconKey = "crown",
            rewardCredits = 500,
            maxProgress = 9
        )
    )

    fun computeAchievements(
        unlockedAchievementIds: Set<String>,
        completedLevelIds: Set<Int>,
        hasExecutedStep: Boolean,
        hasOpenedCodex: Boolean,
        hasUsedHighSpeed: Boolean,
        resolvedEventIds: Set<String>
    ): List<Achievement> {
        return allAchievements.map { base ->
            val isAlreadyUnlocked = unlockedAchievementIds.contains(base.id)
            val shouldUnlock = when (base.id) {
                "ach_pointer_pro" -> isAlreadyUnlocked || completedLevelIds.contains(6) || completedLevelIds.contains(7)
                "ach_memory_master" -> isAlreadyUnlocked || completedLevelIds.contains(8) || resolvedEventIds.contains("evt_mem_leak")
                "ach_first_compile" -> isAlreadyUnlocked || hasExecutedStep
                "ach_stack_commander" -> isAlreadyUnlocked || completedLevelIds.contains(5)
                "ach_loop_wizard" -> isAlreadyUnlocked || completedLevelIds.contains(4)
                "ach_bug_hunter" -> isAlreadyUnlocked || resolvedEventIds.isNotEmpty()
                "ach_struct_specialist" -> isAlreadyUnlocked || completedLevelIds.contains(9)
                "ach_overclocked" -> isAlreadyUnlocked || hasUsedHighSpeed
                "ach_codex_scholar" -> isAlreadyUnlocked || hasOpenedCodex
                "ach_kernel_legend" -> isAlreadyUnlocked || completedLevelIds.size >= 9
                else -> isAlreadyUnlocked
            }

            val progress = when (base.id) {
                "ach_kernel_legend" -> completedLevelIds.size.coerceAtMost(9)
                "ach_pointer_pro" -> if (shouldUnlock) 1 else (if (completedLevelIds.contains(6) || completedLevelIds.contains(7)) 1 else 0)
                "ach_memory_master" -> if (shouldUnlock) 1 else 0
                "ach_bug_hunter" -> resolvedEventIds.size.coerceAtMost(1)
                else -> if (shouldUnlock) 1 else 0
            }

            base.copy(
                isUnlocked = shouldUnlock,
                currentProgress = progress
            )
        }
    }
}
