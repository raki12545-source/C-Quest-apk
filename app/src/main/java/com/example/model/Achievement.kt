package com.example.model

enum class BadgeTier(
    val label: String,
    val containerColorHex: Long,
    val contentColorHex: Long,
    val borderColorHex: Long
) {
    BRONZE("Bronze", 0xFFFEF3C7, 0xFF92400E, 0xFFF59E0B),
    SILVER("Silver", 0xFFF1F5F9, 0xFF334155, 0xFF94A3B8),
    GOLD("Gold", 0xFFFEF08A, 0xFF854D0E, 0xFFEAB308),
    DIAMOND("Diamond", 0xFFCFFAFE, 0xFF0E7490, 0xFF06B6D4),
    LEGENDARY("Kernel Legend", 0xFFEDE9FE, 0xFF5B21B6, 0xFF8B5CF6)
}

data class Achievement(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val badgeTier: BadgeTier,
    val category: String, // "Pointers", "Memory", "Core", "Events", "Mastery"
    val iconKey: String, // "pointer_pro", "memory_master", "compile", "stack", "loop", "bug", "struct", "speed", "codex", "crown"
    val rewardCredits: Int,
    val isUnlocked: Boolean = false,
    val unlockedAtTimestamp: Long? = null,
    val currentProgress: Int = 0,
    val maxProgress: Int = 1
)
