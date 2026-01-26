package fr.cesizen.domain.aggregates.pages

import kotlinx.serialization.Serializable

@Serializable
data class Page(
    val title   : String,
    val content : String,
    val links   : Links,
)