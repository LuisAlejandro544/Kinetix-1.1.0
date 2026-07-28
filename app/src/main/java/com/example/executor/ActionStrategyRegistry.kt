package com.example.executor

import com.example.data.ActionType
import com.example.executor.strategies.*

object ActionStrategyRegistry {
    val strategies: Map<ActionType, ActionStrategy> = 
        AudioVisualStrategiesRegistry.strategies +
        DeveloperStrategiesRegistry.strategies +
        FileStrategiesRegistry.strategies +
        PowerAndroidStrategiesRegistry.strategies +
        SystemStrategiesRegistry.strategies +
        TextAndMathStrategiesRegistry.strategies
}
