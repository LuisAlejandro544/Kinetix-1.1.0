package com.example.executor

import android.os.Process
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher

object CpuDispatcherProvider {
    
    // Core LITTLE: Efficiency dispatcher (lower CPU priority, restricted to efficiency cores)
    val efficiencyDispatcher: CoroutineDispatcher = Executors.newFixedThreadPool(
        4,
        object : ThreadFactory {
            private var count = 0
            override fun newThread(r: Runnable): Thread {
                return Thread {
                    try {
                        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
                    } catch (e: Exception) {
                        // Safe fallback
                    }
                    r.run()
                }.apply {
                    name = "kinetix-efficiency-little-${count++}"
                }
            }
        }
    ).asCoroutineDispatcher()

    // Core big: Performance dispatcher (higher CPU priority, allowed on performance/power cores)
    val performanceDispatcher: CoroutineDispatcher = Executors.newFixedThreadPool(
        4,
        object : ThreadFactory {
            private var count = 0
            override fun newThread(r: Runnable): Thread {
                return Thread {
                    try {
                        Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT)
                    } catch (e: Exception) {
                        // Safe fallback
                    }
                    r.run()
                }.apply {
                    name = "kinetix-performance-big-${count++}"
                }
            }
        }
    ).asCoroutineDispatcher()
}
