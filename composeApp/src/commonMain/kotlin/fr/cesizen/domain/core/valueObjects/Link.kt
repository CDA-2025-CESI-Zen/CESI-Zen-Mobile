package fr.cesizen.domain.core.valueObjects
import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val href   : String,
    val title  : String?    = null,
    val method : HttpMethod = HttpMethod.GET
)
