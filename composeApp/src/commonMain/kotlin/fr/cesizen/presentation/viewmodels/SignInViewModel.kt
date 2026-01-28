package fr.cesizen.presentation.viewmodels

import androidx.lifecycle.ViewModel
import fr.cesizen.domain.aggregates.users.User
import fr.cesizen.domain.core.valueObjects.HttpMethod
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.infrastructure.services.ApiService
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

class SignInViewModel(
    private val apiService: ApiService
) : ViewModel() {

    @Serializable data class SignInDto(val mailAddress: String, val password: String)

    /** Tries to sign in using a [mailAddress] and [password]. */
    suspend fun trySignIn(mailAddress: String, password: String) =
        this.apiService
            .tryRequest<SignInDto, User>(Link(href = "/auth", method = HttpMethod.POST), SignInDto(mailAddress, password))
            .onSuccess { session -> this.apiService.session.update { session }}


}