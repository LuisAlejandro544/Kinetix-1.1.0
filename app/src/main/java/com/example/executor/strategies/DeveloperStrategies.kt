package com.example.executor.strategies

import com.example.executor.ActionStrategy

object DeveloperStrategiesRegistry {
    val strategies: Map<com.example.data.ActionType, ActionStrategy> = mapOf(
        com.example.data.ActionType.EXEC_JAVASCRIPT to ExecJavaScriptStrategy(),
        com.example.data.ActionType.TERMUX_COMMAND to TermuxCommandStrategy(),
        com.example.data.ActionType.CUSTOM_CODE to CustomCodeStrategy(),
        com.example.data.ActionType.HTTP_REQUEST to HttpRequestStrategy()
    )
}
