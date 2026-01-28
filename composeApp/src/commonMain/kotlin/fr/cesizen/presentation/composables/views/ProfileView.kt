package fr.cesizen.presentation.composables.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.cesizen.presentation.services.ToastService
import fr.cesizen.presentation.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileView(
    onNavigateBackward : () -> Unit,
    onSignedOut : () -> Unit,
    toastService : ToastService = koinInject(),
    viewModel : ProfileViewModel = koinViewModel(),
    modifier : Modifier = Modifier,
) {
    AuthView(
        title = "Mon compte",
        header = "Mes informations",
        onNavigateBackward = onNavigateBackward,
        onSignedOut = onSignedOut,
        modifier = modifier,
    ) {
        val coroutineScope = rememberCoroutineScope()
        val session by viewModel.session.collectAsState()

        var editAccount by remember(session) { mutableStateOf(false) }
        var closeAccountDialog by remember(session) { mutableStateOf(false) }

        var mailAddress        by remember(editAccount, session) { mutableStateOf(session!!.mailAddress) }
        var password           by remember(editAccount, session) { mutableStateOf("") }
        var oldPassword        by remember(editAccount, session) { mutableStateOf("") }
        var passwordVisibility by remember(editAccount, session) { mutableStateOf(false) }

        val emailRegex = Regex("""^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-||_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+([a-z]+|\d|-|\.{0,1}|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])?([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$""")

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TextField(
                value           = "Créé le ${session!!.firstActivity}",
                onValueChange   = {},
                singleLine      = true,
                readOnly        = true,
                modifier        = Modifier.fillMaxWidth(),
                leadingIcon     = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Première connexion"
                    )
                }
            )
            TextField(
                value           = mailAddress,
                onValueChange   = { mailAddress = it },
                placeholder     = { Text("adresse mail") },
                singleLine      = true,
                readOnly        = !editAccount,
                isError         = !(mailAddress.takeIf { it.isNotBlank() }?.matches(emailRegex) ?: true),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                modifier        = Modifier.fillMaxWidth(),
                leadingIcon     = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Adresse électronique"
                    )
                },
            )
            if (editAccount) {
                TextField(
                    value         = password,
                    onValueChange = { password = it },
                    placeholder   = { Text("nouveau mot de passe") },
                    singleLine    = true,
                    trailingIcon  = {
                        IconButton(
                            onClick = { passwordVisibility = !passwordVisibility },
                            enabled = password.isNotBlank(),
                        ) { Icon(
                            imageVector        = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Afficher le mot de passe",
                            modifier           = Modifier.size(24.dp)
                        )}
                    },
                    visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    modifier             = Modifier.fillMaxWidth(),
                )
                TextField(
                    value         = oldPassword,
                    onValueChange = { oldPassword = it },
                    placeholder   = { Text("ancien mot de passe") },
                    singleLine    = true,
                    trailingIcon  = {
                        IconButton(
                            onClick = { passwordVisibility = !passwordVisibility },
                            enabled = oldPassword.isNotBlank(),
                        ) { Icon(
                            imageVector        = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Afficher le mot de passe",
                            modifier           = Modifier.size(24.dp)
                        )}
                    },
                    visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    modifier             = Modifier.fillMaxWidth(),
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            when (editAccount) {
                false ->
                    Button(
                        onClick = { editAccount = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Editer mon compte",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                true ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { editAccount = false },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                        ) {
                            Text(
                                text = "Annuler",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Button(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f),
                            enabled = oldPassword.isNotBlank(),
                            onClick = {
                                coroutineScope.launch {
                                    viewModel
                                        .tryUpdateAccount(oldPassword, mailAddress, password)
                                        .onFailure {
                                            it.message?.let { message -> toastService.showToast(message) }
                                        }
                                }
                            },
                        ) {
                            Text(
                                text = "Confirmer",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
            ) {
                Button(
                    onClick = { viewModel.signOut() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Text(
                        text = "Se déconnecter",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Button(
                    onClick = { closeAccountDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    ),
                ) {
                    Text(
                        text = "Fermer mon compte",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }

        if (closeAccountDialog)
            Dialog(onDismissRequest = { closeAccountDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp)
                    ) {
                        Text(
                            text = "Confirmation",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleLarge
                        )
                        TextField(
                            value = oldPassword,
                            onValueChange = { oldPassword = it },
                            placeholder = { Text("ancien mot de passe") },
                            singleLine = true,
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisibility = !passwordVisibility },
                                    enabled = oldPassword.isNotBlank(),
                                ) {
                                    Icon(
                                        imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Afficher le mot de passe",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { closeAccountDialog = false },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = MaterialTheme.colorScheme.onSurface,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                ),
                            ) {
                                Text(
                                    text = "Annuler",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Button(
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f),
                                enabled = oldPassword.isNotBlank(),
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                ),
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.tryCloseAccount(oldPassword).onFailure {
                                            it.message?.let { message ->
                                                toastService.showToast(
                                                    message
                                                )
                                            }
                                        }
                                    }
                                },
                            ) {
                                Text(
                                    text = "Confirmer",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
    }
}