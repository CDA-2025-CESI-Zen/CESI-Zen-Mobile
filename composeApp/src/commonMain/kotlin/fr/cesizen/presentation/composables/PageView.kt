package fr.cesizen.presentation.composables

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.presentation.core.State
import fr.cesizen.presentation.viewmodels.CategoryViewModel
import fr.cesizen.presentation.viewmodels.PageViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageView(
    link                 : Link,
    onBackwardNavigation : (Link) -> Unit,
    viewModel            : PageViewModel = koinViewModel(),
    modifier             : Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        viewModel.load(link)
    }


    Column {
        val state by viewModel.state.collectAsState()
        when (val state = state) {
            is State.Loading -> CircularProgressIndicator(modifier =  Modifier.padding(16.dp).align(Alignment.CenterHorizontally))
            is State.Loaded -> {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 1.dp,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onBackwardNavigation(state.value.links.category) }
                            .padding(16.dp, 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = state.value.title,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = state.value.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                Text(
                    text = state.value.content,
                    textAlign = TextAlign.Justify,
                )
            }
        }
    }
}