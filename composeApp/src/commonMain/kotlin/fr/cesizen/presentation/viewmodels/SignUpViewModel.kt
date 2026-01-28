package fr.cesizen.presentation.viewmodels

import androidx.lifecycle.ViewModel
import fr.cesizen.domain.aggregates.users.User
import fr.cesizen.domain.core.valueObjects.HttpMethod
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.infrastructure.services.ApiService
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

class SignUpViewModel(
    private val apiService: ApiService
) : ViewModel() {

    @Serializable data class SignUpDto(val mailAddress: String, val password: String, val pin: String)

    /** Tries to sign up using the generated [pin] associated with the [mailAddress]. */
    suspend fun trySignUp(mailAddress: String, password: String, pin: String) =
        this.apiService
            .tryRequest<SignUpDto, User>(
                link = Link(href = "/register", method = HttpMethod.POST),
                body = SignUpDto(mailAddress, password, pin)
            ).onSuccess { session -> this.apiService.session.update { session }}

    @Serializable data class RequestRegisterDTO(val mailAddress: String)

    /** Requests the generation of a pin for registration. */
    suspend fun tryRequestRegister(mailAddress: String) =
        this.apiService
            .tryRequest<RequestRegisterDTO, User>(
                link = Link(href = "/request-register", method = HttpMethod.POST),
                body = RequestRegisterDTO(mailAddress)
            )
}