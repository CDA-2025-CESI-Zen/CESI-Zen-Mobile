package fr.cesizen.domain.aggregates.users

import fr.cesizen.domain.core.valueObjects.Link
import kotlinx.serialization.Serializable

@Serializable
data class Links(
    val self                : Link,
    val saveDiagnosisResult : Link,
    val anonymize           : Link,
)