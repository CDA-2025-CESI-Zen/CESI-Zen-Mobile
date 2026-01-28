package fr.cesizen.presentation.composables.views

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import fr.cesizen.infrastructure.services.ApiService
import org.koin.compose.koinInject

@Composable
fun AuthView(
    title : String,
    header : String? = null,
    onSignedOut : () -> Unit,
    onNavigateBackward : () -> Unit,
    api : ApiService = koinInject(),
    modifier : Modifier = Modifier,
    content : @Composable (ColumnScope.() -> Unit),
) {
    val isAuthenticated by api.isAuthenticated.collectAsState(true)
    when (isAuthenticated) {
        true -> View(title, header, onNavigateBackward, modifier, content)
        false -> onSignedOut()
    }
}