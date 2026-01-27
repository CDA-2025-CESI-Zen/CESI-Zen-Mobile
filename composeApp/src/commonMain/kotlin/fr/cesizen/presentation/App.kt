package fr.cesizen.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.presentation.composables.CategoryView
import fr.cesizen.presentation.composables.DiagnosisResultView
import fr.cesizen.presentation.composables.DiagnosisView
import fr.cesizen.presentation.composables.HomeView
import fr.cesizen.presentation.composables.NavBar
import fr.cesizen.presentation.composables.PageView
import fr.cesizen.presentation.composables.Support
import fr.cesizen.presentation.theme.AppTheme
import fr.cesizen.presentation.viewmodels.DiagnosisViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable object Home
@Serializable data class Category(val href : String)
@Serializable data class Page(val href : String)
@Serializable data class Diagnosis(val showResult : Boolean)
@Serializable object Profile

@Composable
@Preview
fun App() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier.padding(24.dp, 64.dp, 24.dp, 32.dp)) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Home) {
                        composable<Home> {
                            HomeView(onNavigateToCategory = { link -> navController.navigate(Category(link.href))  })
                        }
                        composable<Category> {
                            CategoryView(
                                link = Link(it.toRoute<Category>().href),
                                onNavigateBackward = { navController.navigate(Home) },
                                onNavigateToPage = { link -> navController.navigate(Page(link.href)) }
                            )
                        }
                        composable<Page> {
                            PageView(
                                link = Link(it.toRoute<Page>().href),
                                onNavigateBackward = { link -> navController.navigate(Category(link.href)) },
                            )
                        }
                        composable<Diagnosis> {

                            val backStackEntry = remember(it) { navController.getBackStackEntry(Diagnosis(false)) }
                            val viewModel = koinViewModel<DiagnosisViewModel>(viewModelStoreOwner = backStackEntry)
                            when (it.toRoute<Diagnosis>().showResult) {
                                false -> DiagnosisView(
                                    onNavigateToDiagnosisResult = { navController.navigate(Diagnosis(true)) },
                                    onNavigateBackward = { navController.navigate(Home) },
                                    viewModel = viewModel
                                )
                                true -> DiagnosisResultView(
                                    onNavigateBackward = { navController.navigate(Diagnosis(false)) },
                                    viewModel = viewModel
                                )
                            }
                        }
                        composable<Profile> {  }
                    }

                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        Support(
                            onSupportPressed = {  },
                            modifier = Modifier.padding(8.dp)
                        )
                        NavBar(
                            onNavigateToHome = { navController.navigate(Home) },
                            onNavigateToNewDiagnosis = { navController.navigate(Diagnosis(false)) },
                            onNavigateToProfile = { navController.navigate(Profile) },
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}