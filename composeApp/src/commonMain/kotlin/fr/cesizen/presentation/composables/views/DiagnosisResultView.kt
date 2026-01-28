package fr.cesizen.presentation.composables.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.cesizen.presentation.services.ToastService
import fr.cesizen.presentation.viewmodels.DiagnosisViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiagnosisResultView(
    onNavigateBackward : () -> Unit,
    toastService : ToastService = koinInject(),
    viewModel : DiagnosisViewModel = koinViewModel(),
    modifier : Modifier = Modifier,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val isAuthenticated by viewModel.isAuthenticated.collectAsState(false)
        val analysis by viewModel.analysis.collectAsState(null)
        val previousScore by viewModel.previousScore.collectAsState(null)
        val currentScore by viewModel.currentScore.collectAsState(null)
        val totalScore by viewModel.totalScore.collectAsState(null)

        val coroutineScope = rememberCoroutineScope()
        val changeRate = remember(currentScore) {
            previousScore?.takeIf { it != 0 }?.let { previousScore ->
                currentScore?.let { currentScore ->
                    (((currentScore - previousScore) / previousScore.toFloat()) * 100).toInt()
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 1.dp,
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onNavigateBackward() }
                    .padding(16.dp, 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Retour",
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "Diagnostic",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxSize().padding(16.dp)
        ) {
            Text(
                text = "Résultat",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge
            )
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (currentScore != null && totalScore != null)
                        Text(
                            text = buildAnnotatedString {
                                append("Votre score est de ")
                                pushStyle(style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary)
                                )
                                append("$currentScore/$totalScore")

                                pop()
                                appendLine(".")

                                if (changeRate != null) {
                                    pushStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary))
                                    append("(${if (changeRate >= 0) "+" else ""}$changeRate% depuis la dernière fois)")
                                }
                            },
                        )
                    if (analysis == null)
                        Text(
                            text = "Aucune analyse disponible !",
                            style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    else Text(text = analysis!!.content)
                }
            }

            var resultSaved by remember { mutableStateOf(false) }
            if (isAuthenticated)
                Button(
                    enabled = !resultSaved,
                    onClick = {
                        coroutineScope.launch {
                            viewModel.trySaveResult().fold(
                                onSuccess = {
                                    resultSaved = true
                                },
                                onFailure = {
                                    it.message?.let { message ->
                                        toastService.showToast(message)
                                    }
                                }
                            )
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "SAUVEGARDER",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
        }
    }
}