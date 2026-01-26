package fr.cesizen.domain.aggregates.pages

import fr.cesizen.domain.core.valueObjects.Link
import kotlinx.serialization.Serializable

@Serializable
data class Links(
    val self     : Link,
    val category : Link,
)