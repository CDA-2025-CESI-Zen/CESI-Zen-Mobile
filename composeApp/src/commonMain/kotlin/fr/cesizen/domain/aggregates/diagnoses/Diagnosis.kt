package fr.cesizen.domain.aggregates.diagnoses

import kotlinx.serialization.Serializable

@Serializable
data class Diagnosis(
    val items          : List<DiagnosisItem>     = emptyList(),
    val analyses       : List<DiagnosisAnalysis> = emptyList(),
    val checkedItemIds : Set<UInt>,
    val lastScore      : Int? = null,
) {
    fun withItemCheck(id: UInt, value: Boolean = true) =
        this.copy(checkedItemIds = if (value) checkedItemIds + id else checkedItemIds - id)

    fun withLastScore(value : Int?) =
        this.copy(lastScore = value)
}