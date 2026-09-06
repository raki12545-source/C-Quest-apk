package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("C-Kernel Sim", appName)
  }

  @Test
  fun `achievements include Pointer Pro and Memory Master and unlock correctly`() {
    val achievements = com.example.data.AchievementRepository.allAchievements
    val pointerPro = achievements.find { it.id == "ach_pointer_pro" }
    val memoryMaster = achievements.find { it.id == "ach_memory_master" }

    org.junit.Assert.assertNotNull(pointerPro)
    org.junit.Assert.assertNotNull(memoryMaster)
    assertEquals("Pointer Pro", pointerPro?.title)
    assertEquals("Memory Master", memoryMaster?.title)

    // Verify evaluation logic for Pointer Pro (completed level 6 or 7)
    val unlocked = com.example.data.AchievementRepository.computeAchievements(
        unlockedAchievementIds = emptySet(),
        completedLevelIds = setOf(6),
        hasExecutedStep = true,
        hasOpenedCodex = false,
        hasUsedHighSpeed = false,
        resolvedEventIds = emptySet()
    )
    val pointerProUnlocked = unlocked.find { it.id == "ach_pointer_pro" }
    assertEquals(true, pointerProUnlocked?.isUnlocked)

    // Verify evaluation logic for Memory Master (completed level 8 or resolved memory leak event)
    val memoryMasterUnlocked = com.example.data.AchievementRepository.computeAchievements(
        unlockedAchievementIds = emptySet(),
        completedLevelIds = emptySet(),
        hasExecutedStep = true,
        hasOpenedCodex = false,
        hasUsedHighSpeed = false,
        resolvedEventIds = setOf("evt_mem_leak")
    )
    val memMasterResult = memoryMasterUnlocked.find { it.id == "ach_memory_master" }
    assertEquals(true, memMasterResult?.isUnlocked)
  }
}
