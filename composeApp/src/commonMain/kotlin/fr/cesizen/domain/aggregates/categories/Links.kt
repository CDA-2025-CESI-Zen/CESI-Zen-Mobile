package fr.cesizen.domain.aggregates.categories

import fr.cesizen.domain.core.valueObjects.Link
import kotlinx.serialization.Serializable

@Serializable
data class Links(
    val self  : Link,
    val pages : List<Link>,
)