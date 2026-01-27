package fr.cesizen.infrastructure.services

import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.infrastructure.valueObjects.ApiException
import fr.cesizen.infrastructure.valueObjects.ApiResponse
import fr.cesizen.infrastructure.valueObjects.Session
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import fr.cesizen.domain.core.valueObjects.HttpMethod as CesiZenHttpMethod

class ApiService(
    val client : HttpClient,
) {

    companion object {
        const val API_URL : String = "http://10.0.2.2:5000"
    }

    val session = MutableStateFlow<Session?>(null)
    val isAuthenticated = this.session.map { it != null }

    suspend inline fun <reified In, reified Out> tryRequest(
        link : Link,
        body : In? = null
    ): Result<Out> =
        safeRequest {
            client.request(API_URL + link.href) {
                method = link.method.toKtor()
                contentType(ContentType.Application.Json)

                body?.let { setBody(it) }
            }
        }

    suspend inline fun <reified In, reified Out> tryRequestWithSession(
        noinline link : Session.() -> Link,
                 body : In? = null
    ): Result<Out> {
        val currentSession = session.value
            ?: return Result.failure(IllegalStateException("Vous n'êtes pas connecté(e) !"))

        val resolvedLink = currentSession.link()

        return safeRequest<Out> {
            client.request(resolvedLink.href) {

                method = resolvedLink.method.toKtor()
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer ${currentSession.token}")

                body?.let { setBody(it) }
            }
        }.onFailure {
            if (it is ApiException.Unauthorized) {
                session.update { null }
            }
        }
    }

    suspend inline fun <reified T> safeRequest(
        crossinline request: suspend () -> HttpResponse
    ): Result<T> {
        val response = request()

        if (response.status.isSuccess())
            return response.body<ApiResponse<T>>().asResult()

        return try {
            response.body<ApiResponse<T>>().asResult()
        } catch (_ : Exception) {
            when (response.status) {
                HttpStatusCode.Unauthorized -> Result.failure(ApiException.Unauthorized())
                HttpStatusCode.Forbidden    -> Result.failure(ApiException.Forbidden())
                in HttpStatusCode.InternalServerError..HttpStatusCode.RequestTimeout ->
                    Result.failure(ApiException.ServerError())

                else -> Result.failure(ApiException.Unknown())
            }
        }
    }

    fun CesiZenHttpMethod.toKtor(): HttpMethod =
        when (this) {
            CesiZenHttpMethod.GET    -> HttpMethod.Get
            CesiZenHttpMethod.POST   -> HttpMethod.Post
            CesiZenHttpMethod.PUT    -> HttpMethod.Put
            CesiZenHttpMethod.PATCH  -> HttpMethod.Patch
            CesiZenHttpMethod.DELETE -> HttpMethod.Delete
        }
}
