package fr.cesizen.presentation.viewmodels

import androidx.lifecycle.ViewModel
import fr.cesizen.domain.aggregates.pages.Page
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.infrastructure.services.ApiService
import fr.cesizen.presentation.core.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PageViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val _state = MutableStateFlow<State<Page>>(State.Loading())
    val state = this._state.asStateFlow()

    suspend fun load(link : Link) =
        this.apiService
            .tryRequest<Unit, Page>(link)
            .onSuccess { page -> _state.update { State.Loaded(page) }}

}