package fr.cesizen.domain.aggregates.diagnoses

import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisItem(
    val id    : UInt,
    val label : String,
    val score : Int,
)