package fr.cesizen.presentation.composables.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.presentation.core.State
import fr.cesizen.presentation.services.ToastService
import fr.cesizen.presentation.viewmodels.CategoryViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CategoryView(
    link : Link,
    onNavigateBackward : () -> Unit,
    onNavigateToPage : (Link) -> Unit,
    toastService : ToastService = koinInject(),
    viewModel : CategoryViewModel = koinViewModel(),
    modifier : Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        viewModel.tryLoad(link).onFailure {
            it.message?.let {
                toastService.showToast(it)
            }
        }
    }

    val category by viewModel.category.collectAsState()
    when (val category = category) {
        is State.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        is State.Loaded ->
            View(
                title = category.value.title,
                header = "Pages",
                onNavigateBackward = onNavigateBackward,
                modifier = modifier
            ) {

                if (category.value.links.pages.isEmpty())
                    Text(
                        text = "Aucune page dans cette catégorie !",
                        style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                    )
                else Surface(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyColumn(modifier = Modifier.padding(16.dp, 8.dp)) {
                        items(category.value.links.pages) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { onNavigateToPage(it) }
                                    .padding(16.dp, 8.dp)
                            ) {
                                Text(
                                    text  = it.title ?: "Page sans titre",
                                    style = MaterialTheme.typography.titleMedium,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = it.title,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
            }
        }
    }
}