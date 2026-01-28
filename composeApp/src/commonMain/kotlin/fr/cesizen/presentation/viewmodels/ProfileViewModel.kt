package fr.cesizen.presentation.viewmodels

import androidx.lifecycle.ViewModel
import fr.cesizen.domain.aggregates.users.User
import fr.cesizen.domain.core.valueObjects.HttpMethod
import fr.cesizen.infrastructure.services.ApiService
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

class ProfileViewModel(
    private val apiService: ApiService
) : ViewModel() {

    val session = this.apiService.session

    @Serializable data class UpdateAccountDto(
        val password: String,
        val newMailAddress: String?,
        val newPassword: String?
    )

    /** Tries to update the account information. */
    suspend fun tryUpdateAccount(
        password: String,
        newMailAddress: String,
        newPassword: String,
    ) = this.apiService
        .tryRequestWithSession<UpdateAccountDto, User>(
            link = { this.links.self.copy(method = HttpMethod.PATCH) },
            body = UpdateAccountDto(password, newMailAddress.takeIf { it != this.apiService.session.value!!.mailAddress }, newPassword.takeIf { it.isNotBlank() })
        ).onSuccess { session -> this.apiService.session.update { session }}

    /** Stops the current session. */
    fun signOut() =
        this.apiService.session.update { null }

    @Serializable data class CloseAccountDto(val password: String)

    /** Tries to close and anonymize the account. */
    suspend fun tryCloseAccount(password: String) =
        this.apiService
            .tryRequestWithSession<CloseAccountDto, Unit>(
                link = { this.links.anonymize },
                body = CloseAccountDto(password)
            ).onSuccess { this.apiService.session.update { null }}

}