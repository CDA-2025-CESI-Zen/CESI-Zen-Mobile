package fr.cesizen.infrastructure.valueObjects

import fr.cesizen.domain.aggregates.users.User
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val token : String,
    val user  : User
)
