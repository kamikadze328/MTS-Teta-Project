package com.kamikadze328.benchmark

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

    private fun startup(startupMode: StartupMode) = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(StartupTimingMetric()),
        compilationMode = CompilationMode.SpeedProfile(),
        iterations = ITERATIONS,
        startupMode = startupMode,
        setupBlock = {
            pressHome()
        }
    ) {
        startActivityAndWait()
    }

    @Test
    fun startupCold() = startup(StartupMode.COLD)

    @Test
    fun startupHot() = startup(StartupMode.HOT)

    @Test
    fun startupWarm() = startup(StartupMode.WARM)

}
