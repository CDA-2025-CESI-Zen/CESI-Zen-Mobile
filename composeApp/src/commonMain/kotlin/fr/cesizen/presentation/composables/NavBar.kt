package fr.cesizen.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.cesizen.infrastructure.services.ApiService
import org.koin.compose.koinInject


@Composable
fun NavBar(
    onNavigateToHome : () -> Unit,
    onNavigateToNewDiagnosis : () -> Unit,
    onNavigateToSignIn : () -> Unit,
    onNavigateToProfile : () -> Unit,
    api : ApiService = koinInject(),
    modifier : Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(24.dp, 8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).clickable { onNavigateToHome() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Accueil",
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "ACCUEIL",
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).clickable { onNavigateToNewDiagnosis() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                    contentDescription = "Nouveau diagnostic",
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "DIAGNOSTIC",
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }

            val isAuthenticated by api.isAuthenticated.collectAsState(false)
            when (isAuthenticated) {
                false -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f).clickable { onNavigateToSignIn() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Se connecter",
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "CONNEXION",
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
                true -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f).clickable { onNavigateToProfile() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Profil",
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "PROFIL",
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}