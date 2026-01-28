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
import androidx.navigation.compose.rememberNavController
import fr.cesizen.presentation.composables.Diagnosis
import fr.cesizen.presentation.composables.HandleViews
import fr.cesizen.presentation.composables.Home
import fr.cesizen.presentation.composables.NavBar
import fr.cesizen.presentation.composables.Profile
import fr.cesizen.presentation.composables.SignIn
import fr.cesizen.presentation.composables.Support
import fr.cesizen.presentation.theme.AppTheme


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

                    val navViewController = rememberNavController()
                    navViewController.HandleViews()

                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        Support(
                            onSupportPressed = {  },
                            modifier = Modifier.padding(8.dp)
                        )
                        NavBar(
                            onNavigateToHome = { navViewController.navigate(Home) },
                            onNavigateToNewDiagnosis = { navViewController.navigate(Diagnosis(false)) },
                            onNavigateToSignIn = { navViewController.navigate(SignIn) },
                            onNavigateToProfile = { navViewController.navigate(Profile) },
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}