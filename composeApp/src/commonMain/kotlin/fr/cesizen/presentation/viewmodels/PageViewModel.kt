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

    private val _page = MutableStateFlow<State<Page>>(State.Loading())
    val page = this._page.asStateFlow()

    suspend fun load(link : Link) =
        this.apiService
            .tryRequest<Unit, Page>(link)
            .onSuccess { page -> _page.update { State.Loaded(page) }}

}