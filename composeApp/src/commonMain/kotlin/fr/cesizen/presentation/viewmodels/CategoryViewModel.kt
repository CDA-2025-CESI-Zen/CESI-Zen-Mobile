package fr.cesizen.presentation.viewmodels

import androidx.lifecycle.ViewModel
import fr.cesizen.domain.aggregates.categories.Category
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.infrastructure.services.ApiService
import fr.cesizen.presentation.core.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CategoryViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val _state = MutableStateFlow<State<Category>>(State.Loading())
    val state = this._state.asStateFlow()

    suspend fun load(link : Link) =
        this.apiService
            .tryRequest<Unit, Category>(link)
            .onSuccess { category -> _state.update { State.Loaded(category) }}

}