package com.example.data

data class ActionData(
    val type: ActionType,
    val params: Map<String, String> = emptyMap()
)
