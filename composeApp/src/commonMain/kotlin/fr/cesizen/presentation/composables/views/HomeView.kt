package fr.cesizen.presentation.composables.views

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import cesizen.composeapp.generated.resources.Res
import cesizen.composeapp.generated.resources.cesizen_logo
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.presentation.core.State
import fr.cesizen.presentation.viewmodels.CategoriesViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeView(
    onNavigateToCategory : (Link) -> Unit,
    viewModel : CategoriesViewModel = koinViewModel(),
    modifier : Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(Res.drawable.cesizen_logo),
            contentDescription = "Logo CESI Zen",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
        Text(
            text = "S'informer",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge
        )
        val categories by viewModel.categories.collectAsState()
        when (val categories = categories) {
            is State.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            is State.Loaded ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (categories.value.isEmpty())
                        Text(
                            text = "Aucune catégorie",
                            style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                            modifier = Modifier.padding(16.dp, 8.dp)
                        )
                    else
                        LazyColumn {
                            items(categories.value) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable { onNavigateToCategory(it.links.self) }
                                        .padding(16.dp, 8.dp)
                                ) {
                                    Text(
                                        text  = it.title,
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