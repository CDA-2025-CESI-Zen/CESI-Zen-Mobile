package fr.cesizen.infrastructure.valueObjects

import kotlinx.serialization.Serializable

/**
 * Data structure representing a response from a HTTP request to the API.
 * @param successful Indicates if the request is successful
 * @param errorMessage Transferred error message
 * @param value Transferred data
 */
@Serializable
data class ApiResponse<T>(
    val successful   : Boolean = false,
    val errorMessage : String? = null,
    val value        : T?      = null,
) {
    fun asResult() : Result<T> =
        if (this.successful && this.value != null) Result.success(this.value)
        else Result.failure(Exception(this.errorMessage))
}
