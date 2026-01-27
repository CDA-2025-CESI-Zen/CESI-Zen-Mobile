package fr.cesizen.domain.aggregates.users

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val mailAddress         : String,
    val lastDiagnosisResult : Int?,
    val links               : Links,
)