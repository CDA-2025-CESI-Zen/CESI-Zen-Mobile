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
import fr.cesizen.presentation.composables.HomeView
import fr.cesizen.presentation.composables.NavBar
import fr.cesizen.presentation.composables.PageView
import fr.cesizen.presentation.composables.Support
import fr.cesizen.presentation.theme.AppTheme
import kotlinx.serialization.Serializable

@Serializable
object Home
@Serializable
data class Category(val href : String)
@Serializable
data class Page(val href : String)
@Serializable
object Diagnosis
@Serializable
object Profile

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
                            HomeView(onCategoryNavigation = { link -> navController.navigate(Category(link.href))  })
                        }
                        composable<Category> {
                            CategoryView(
                                link = Link(it.toRoute<Category>().href),
                                onBackwardNavigation = { navController.navigate(Home) },
                                onPageNavigation = { link -> navController.navigate(Page(link.href)) }
                            )
                        }
                        composable<Page> {
                            PageView(
                                link = Link(it.toRoute<Page>().href),
                                onBackwardNavigation = { link -> navController.navigate(Category(link.href)) },
                            )
                        }
                        composable<Diagnosis> {  }
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
                            onHomePressed = { navController.navigate(Home) },
                            onNewDiagnosticPressed = { navController.navigate(Diagnosis) },
                            onProfilePressed = { navController.navigate(Profile) },
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}