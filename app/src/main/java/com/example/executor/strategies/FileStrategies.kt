package com.example.executor.strategies

import com.example.executor.ActionStrategy

object FileStrategiesRegistry {
    val strategies: Map<com.example.data.ActionType, ActionStrategy> = mapOf(
        com.example.data.ActionType.WRITE_FILE to WriteFileStrategy(),
        com.example.data.ActionType.READ_FILE to ReadFileStrategy(),
        com.example.data.ActionType.APPEND_FILE to AppendFileStrategy()
    )
}
