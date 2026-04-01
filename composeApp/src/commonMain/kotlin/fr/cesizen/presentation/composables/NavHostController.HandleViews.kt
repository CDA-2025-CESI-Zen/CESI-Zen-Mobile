package fr.cesizen.presentation.composables

import ForgottenPasswordView
import SignInView
import SignUpView
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import fr.cesizen.domain.core.valueObjects.Link
import fr.cesizen.presentation.composables.views.CategoryView
import fr.cesizen.presentation.composables.views.DiagnosisResultView
import fr.cesizen.presentation.composables.views.DiagnosisView
import fr.cesizen.presentation.composables.views.HomeView
import fr.cesizen.presentation.composables.views.PageView
import fr.cesizen.presentation.composables.views.ProfileView
import fr.cesizen.presentation.composables.views.SupportView
import fr.cesizen.presentation.viewmodels.DiagnosisViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable object Home
@Serializable data class Category(val href : String)
@Serializable data class Page(val href : String)
@Serializable data class Diagnosis(val showResult : Boolean)
@Serializable object Profile
@Serializable object SignIn
@Serializable object ForgottenPassword
@Serializable object SignUp
@Serializable object Support

@Composable
fun NavHostController.HandleViews() {

    val navController = this
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = Modifier.padding(bottom = 148.dp)
    ) {
        composable<Home> {
            HomeView(onNavigateToCategory = { link -> navController.navigate(Category(link.href)) })
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
        composable<Profile> {
            ProfileView(
                onNavigateBackward = { navController.navigate(Home) },
                onSignedOut = { navController.navigate(SignIn) }
            )
        }
        composable<SignIn> {
            SignInView(
                onNavigateBackward = { navController.navigate(Home) },
                onNavigateToForgottenPassword = { navController.navigate(ForgottenPassword) },
                onNavigateToSignUp = { navController.navigate(SignUp) },
                onSignIn = { navController.navigate(Profile) }
            )
        }
        composable<ForgottenPassword> {
            ForgottenPasswordView(
                onNavigateBackward = { navController.navigate(SignIn) },
                onSignIn = { navController.navigate(Profile) }
            )
        }
        composable<SignUp> {
            SignUpView(
                onNavigateBackward = { navController.navigate(SignIn) },
                onNavigateToSignIn = { navController.navigate(SignIn) },
                onSignUp = { navController.navigate(Profile) }
            )
        }
        composable<Support> {
            SupportView(
                onNavigateBackward = { navController.navigate(Home) },
            )
        }
    }
}