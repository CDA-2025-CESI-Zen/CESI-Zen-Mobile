package fr.cesizen.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.cesizen.domain.aggregates.categories.Category
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.infrastructure.services.ApiService
import fr.cesizen.presentation.core.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val apiService: ApiService
) : ViewModel() {
    private val _categories = MutableStateFlow<State<List<Category>>>(State.Loading())
    val categories = this._categories.asStateFlow()

    init {
        viewModelScope.launch {
            apiService
                .tryRequest<Unit, List<Category>>(Link("/categories"))
                .onSuccess { categories -> _categories.update { State.Loaded(categories) }}
        }
    }
}