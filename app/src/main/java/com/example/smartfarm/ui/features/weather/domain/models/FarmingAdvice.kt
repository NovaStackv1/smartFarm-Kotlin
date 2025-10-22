package com.example.smartfarm.ui.features.weather.domain.models

data class FarmingAdvice(
    val message: String,
    val type: AdviceType,
    val priority: Int
)