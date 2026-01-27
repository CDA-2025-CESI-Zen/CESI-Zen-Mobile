package fr.cesizen.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.presentation.core.State
import fr.cesizen.presentation.viewmodels.CategoryViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CategoryView(
    link               : Link,
    onNavigateBackward : () -> Unit,
    onNavigateToPage   : (Link) -> Unit,
    viewModel          : CategoryViewModel = koinViewModel(),
    modifier           : Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        viewModel.load(link)
    }

    val category by viewModel.category.collectAsState()
    when (val category = category) {
        is State.Loading -> CircularProgressIndicator()
        is State.Loaded ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                            contentDescription = category.value.title,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = category.value.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier.fillMaxSize().padding(16.dp)
                ) {
                    Text(
                        text = "Pages",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleLarge
                    )
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
                                    modifier = Modifier.clickable { onNavigateToPage(it) }
                                ) {
                                    Text(
                                        text  = it.title ?: "Page sans titre",
                                        style = MaterialTheme.typography.titleMedium
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
}