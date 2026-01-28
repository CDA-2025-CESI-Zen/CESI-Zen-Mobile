import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import fr.cesizen.presentation.composables.views.View
import fr.cesizen.presentation.services.ToastService
import fr.cesizen.presentation.viewmodels.SignInViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInView(
    onNavigateBackward : () -> Unit,
    onNavigateToForgottenPassword : () -> Unit,
    onNavigateToSignUp : () -> Unit,
    onSignIn : () -> Unit,
    toastService : ToastService = koinInject(),
    viewModel : SignInViewModel = koinViewModel(),
    modifier : Modifier = Modifier,
) {
    View(
        title = "Mon compte",
        header = "Se connecter",
        onNavigateBackward = onNavigateBackward,
        modifier = modifier,
    ) {

        val coroutineScope = rememberCoroutineScope()

        var mailAddress        by remember { mutableStateOf("") }
        var password           by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        val emailRegex = Regex("""^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-||_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+([a-z]+|\d|-|\.{0,1}|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])?([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$""")

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TextField(
                value           = mailAddress,
                onValueChange   = { mailAddress = it },
                placeholder     = { Text("adresse mail") },
                singleLine      = true,
                isError         = !(mailAddress.takeIf { it.isNotBlank() }?.matches(emailRegex) ?: true),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                modifier        = Modifier.fillMaxWidth()
            )
            TextField(
                value         = password,
                onValueChange = { password = it },
                placeholder   = { Text("mot de passe") },
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
                supportingText       = { Text(
                    text     = "mot de passe oublié ?",
                    modifier = Modifier.clickable { onNavigateToForgottenPassword() }
                )}
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onNavigateToSignUp() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "S'enregistrer",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.trySignIn(
                            mailAddress,
                            password
                        ).fold(
                            onSuccess = { onSignIn() },
                            onFailure = { it.message?.let { message -> toastService.showToast(message) }}
                        )
                    }
                },
                enabled = mailAddress.matches(emailRegex) && password.isNotBlank(),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Se connecter",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}