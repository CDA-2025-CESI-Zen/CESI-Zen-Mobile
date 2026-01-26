package fr.cesizen.domain.aggregates.categories

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val title : String,
    val links : Links,
)