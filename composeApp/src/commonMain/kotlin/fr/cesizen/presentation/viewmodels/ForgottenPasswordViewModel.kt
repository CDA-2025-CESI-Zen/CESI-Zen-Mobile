package fr.cesizen.presentation.viewmodels

import androidx.lifecycle.ViewModel
import fr.cesizen.domain.aggregates.users.User
import fr.cesizen.domain.core.valueObjects.HttpMethod
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.infrastructure.services.ApiService
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

class ForgottenPasswordViewModel(
    private val apiService: ApiService
) : ViewModel() {

    @Serializable data class PasswordResetDto(val mailAddress: String, val newPassword: String, val pin: String)

    /** Tries to reset the password using the [pin] associated with the account's [mailAddress]. */
    suspend fun tryResetPassword(mailAddress: String, newPassword: String, pin: String) =
        this.apiService
            .tryRequest<PasswordResetDto, User>(
                link = Link(href = "/reset-password", method = HttpMethod.POST),
                body = PasswordResetDto(mailAddress, newPassword, pin)
            ).onSuccess { session -> this.apiService.session.update { session }}

    @Serializable data class RequestPasswordResetDTO(val mailAddress: String)

    /** Tries to request the generation of a pin associated with the account's [mailAddress]. */
    suspend fun tryRequestPasswordReset(mailAddress: String) =
        this.apiService
            .tryRequest<RequestPasswordResetDTO, Unit>(
                link = Link(href = "/request-password-reset", method = HttpMethod.POST),
                body = RequestPasswordResetDTO(mailAddress)
            )
}