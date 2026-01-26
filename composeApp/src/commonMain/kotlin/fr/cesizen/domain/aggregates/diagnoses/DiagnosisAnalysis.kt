package fr.cesizen.domain.aggregates.diagnoses

import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisAnalysis(
    val scoreThreshold : Int,
    val content        : String,
)