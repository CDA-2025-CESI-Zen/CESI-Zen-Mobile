package fr.cesizen.domain.aggregates.diagnoses

import kotlinx.serialization.Serializable

@Serializable
data class Diagnosis(
    val items    : List<DiagnosisItem>     = emptyList(),
    val analyses : List<DiagnosisAnalysis> = emptyList(),
)