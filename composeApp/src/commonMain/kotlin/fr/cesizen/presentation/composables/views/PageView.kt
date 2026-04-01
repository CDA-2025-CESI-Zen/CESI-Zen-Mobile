package fr.cesizen.presentation.composables.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.presentation.core.State
import fr.cesizen.presentation.services.ToastService
import fr.cesizen.presentation.viewmodels.PageViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageView(
    link : Link,
    onNavigateBackward : (Link) -> Unit,
    toastService : ToastService = koinInject(),
    viewModel : PageViewModel = koinViewModel(),
    modifier : Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        viewModel.tryLoad(link).onFailure {
            it.message?.let {
                toastService.showToast(it)
            }
        }
    }

    val page by viewModel.page.collectAsState()
    when (val page = page) {
        is State.Loading ->
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        is State.Loaded -> {

            View(
                title = page.value.title,
                onNavigateBackward = { onNavigateBackward(page.value.links.category) },
                modifier = modifier
            ) {
                Text(
                    text = page.value.content,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }
        }
    }
}