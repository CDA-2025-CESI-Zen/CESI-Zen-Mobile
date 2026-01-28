package fr.cesizen.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.cesizen.domain.aggregates.diagnoses.Diagnosis
import fr.cesizen.domain.aggregates.diagnoses.DiagnosisAnalysis
import fr.cesizen.domain.aggregates.diagnoses.DiagnosisItem
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.infrastructure.services.ApiService
import fr.cesizen.presentation.core.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class DiagnosisViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val _diagnosis = MutableStateFlow<State<Diagnosis>>(State.Loading())
    val diagnosis = this._diagnosis.asStateFlow()

    private val _checkedItemIds = MutableStateFlow(emptySet<UInt>())
    val checkedItemIds = this._checkedItemIds.asStateFlow()

    val isAuthenticated = this.apiService.isAuthenticated

    /** Total score based on the [diagnosis] items. */
    val totalScore = this._diagnosis.map { diagnosis ->
        when (diagnosis) {
            is State.Loaded -> diagnosis.value.items.sumOf { it.score }
            else -> null
        }
    }

    /** Previous score. Equals to `null` if there is no active session. */
    val previousScore = this.apiService.session.map { it?.lastDiagnosisResult }

    /** Score based on the [diagnosis] items and the [checkedItemIds]. */
    val currentScore = this._diagnosis.combine(this._checkedItemIds) { diagnosis, checkedItemIds ->
        return@combine when (diagnosis) {
            is State.Loaded -> {
                diagnosis.value.items
                    .filter { it.id in checkedItemIds }
                    .sumOf { it.score }

            } else -> null
        }
    }

    /** Analysis based on the [diagnosis] analyses and the [currentScore] */
    val analysis = this._diagnosis.combine(this.currentScore) { diagnosis, currentScore ->
        return@combine when (diagnosis) {
            is State.Loaded -> {
                currentScore?.let { currentScore -> diagnosis.value.analyses
                    .sortedByDescending { it.scoreThreshold }
                    .firstOrNull { currentScore >= it.scoreThreshold }}

            } else -> null
        }
    }

    /** Toggles a diagnosis item on or off. */
    fun toggleItem(id: UInt) =
        this._checkedItemIds.update {
            (if (id in it) it - id else it + id)
        }

    @Serializable data class SaveDiagnosisResultDto(val diagnosisItemIds : Set<UInt>)
    @Serializable data class DiagnosisResultDto(val newScore : Int)

    /** Tries to save the [checkedItemIds] and [currentScore] to the active session. */
    suspend fun trySaveResult() =
        this.apiService.tryRequestWithSession<SaveDiagnosisResultDto, DiagnosisResultDto>(
            link = { this.links.saveDiagnosisResult },
            body = SaveDiagnosisResultDto(checkedItemIds.value)
        ).onSuccess { diagnosisResult ->
            this.apiService.session.update { it!!.copy(lastDiagnosisResult = diagnosisResult.newScore) }
        }


    init {
        viewModelScope.launch {
            apiService
                .tryRequest<Unit, List<DiagnosisItem>>(Link("/diagnosis-items"))
                .onSuccess { diagnosisItems ->

                    apiService
                        .tryRequest<Unit, List<DiagnosisAnalysis>>(Link("/diagnosis-analyses"))
                        .onSuccess { diagnosisAnalyses ->

                            _diagnosis.update {
                                State.Loaded(Diagnosis(
                                    diagnosisItems,
                                    diagnosisAnalyses
                                ))
                            }
                        }
                }
        }
    }
}