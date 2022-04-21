package com.kamikadze328.benchmark

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class StartupBenchmarkStartupModel {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    private fun startup(
        startupMode: StartupMode,
        intentAction: String
    ) = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(StartupTimingMetric()),
        compilationMode = CompilationMode.None(),
        iterations = ITERATIONS,
        startupMode = startupMode,
        setupBlock = {
            pressHome()
        }
    ) {
        val intent = Intent().apply {
            action = "$TARGET_PACKAGE.$intentAction"
        }
        startActivityAndWait(intent)
    }

    @Test
    fun startupColdMovies() = startup(StartupMode.COLD, "MOVIES_ACTION")

    @Test
    fun startupHotMovies() = startup(StartupMode.HOT, "MOVIES_ACTION")

    @Test
    fun startupWarmMovies() = startup(StartupMode.WARM, "MOVIES_ACTION")

    @Test
    fun startupColdProfile() = startup(StartupMode.COLD, "PROFILE_ACTION")

    @Test
    fun startupHotProfile() = startup(StartupMode.HOT, "PROFILE_ACTION")

    @Test
    fun startupWarmProfile() = startup(StartupMode.WARM, "PROFILE_ACTION")

}
