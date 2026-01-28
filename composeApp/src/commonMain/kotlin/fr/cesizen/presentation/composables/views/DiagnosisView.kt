package fr.cesizen.presentation.composables.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.cesizen.presentation.core.State
import fr.cesizen.presentation.viewmodels.DiagnosisViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiagnosisView(
    onNavigateToDiagnosisResult : () -> Unit,
    onNavigateBackward : () -> Unit,
    viewModel : DiagnosisViewModel = koinViewModel(),
    modifier : Modifier = Modifier,
) {
    val diagnosis by viewModel.diagnosis.collectAsState()
    when (val diagnosis = diagnosis) {
        is State.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        is State.Loaded -> {
            View(
                title = "Diagnostic",
                header = "Evènements vécus dans l'année",
                onNavigateBackward = onNavigateBackward,
                modifier = modifier
            ) {
                val checkedItemIds by viewModel.checkedItemIds.collectAsState()
                LazyColumn {
                    items(diagnosis.value.items) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { viewModel.toggleItem(it.id) }
                                .padding(end = 8.dp)
                        ) {
                            RadioButton(
                                selected = it.id in checkedItemIds,
                                onClick = { viewModel.toggleItem(it.id) },
                                colors = RadioButtonDefaults.colors().copy(
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    unselectedColor = MaterialTheme.colorScheme.primary,
                                )
                            )
                            Text(
                                text  = it.label,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    item {
                        Box(modifier = modifier.fillMaxWidth().padding(8.dp)) {
                            Button(
                                onClick = { onNavigateToDiagnosisResult() },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                ),
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(
                                    text = "CONFIRMER",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(256.dp))
                    }
                }
            }
        }
    }
}