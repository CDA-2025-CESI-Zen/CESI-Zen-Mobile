package fr.cesizen.domain.aggregates.users

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val token               : String,
    val mailAddress         : String,
    val firstActivity       : String,
    val lastDiagnosisResult : Int?,
    val links               : Links,
)