import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.cesizen.presentation.composables.views.View
import fr.cesizen.presentation.services.ToastService
import fr.cesizen.presentation.viewmodels.SignUpViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpView(
    onNavigateBackward : () -> Unit,
    onNavigateToSignIn : () -> Unit,
    onSignUp : () -> Unit,
    toastService : ToastService = koinInject(),
    viewModel : SignUpViewModel = koinViewModel(),
    modifier : Modifier = Modifier,
) {
    View(
        title = "Mon compte",
        header = "S'enregistrer",
        onNavigateBackward = onNavigateBackward,
        modifier = modifier,
    ) {

        val coroutineScope = rememberCoroutineScope()

        var mailAddress by remember { mutableStateOf("") }
        var pin         by remember { mutableStateOf("") }

        var password                       by remember { mutableStateOf("") }
        var passwordVisibility             by remember { mutableStateOf(false) }
        var passwordConfirmation           by remember { mutableStateOf("") }
        var passwordConfirmationVisibility by remember { mutableStateOf(false) }
        val emailRegex = Regex("""^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-||_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+([a-z]+|\d|-|\.{0,1}|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])?([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$""")

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TextField(
                value           = mailAddress,
                onValueChange   = { mailAddress = it },
                placeholder     = { Text("adresse mail") },
                isError         = !(mailAddress.takeIf { it.isNotBlank() }?.matches(emailRegex) ?: true),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                singleLine      = true,
                modifier        = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier              = Modifier.fillMaxWidth().height(56.dp)
            ) {
                TextField(
                    value           = pin,
                    onValueChange   = { pin = (it.takeIf { it.all { char -> char.isDigit() } } ?: pin).take(8) },
                    placeholder     = { Text("pin") },
                    isError         = pin.length != 8 || !pin.all { char -> char.isDigit() },
                    singleLine      = true,
                    enabled         = emailRegex.matches(mailAddress),
                    textStyle       = TextStyle.Default.copy(letterSpacing = 4.sp, fontFamily = FontFamily.Monospace),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                    modifier        = Modifier.weight(1f).fillMaxHeight()
                )
                Button(
                    enabled = mailAddress.matches(emailRegex),
                    onClick = {
                        coroutineScope.launch {
                            viewModel.tryRequestRegister(mailAddress).fold(
                                onSuccess = { toastService.showToast("Code PIN envoyé !") },
                                onFailure = {
                                    it.message?.let { message ->
                                        toastService.showToast(message)
                                    }
                                }
                            )
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Send,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Générer le code",
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Générer le code",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
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
                modifier             = Modifier.fillMaxWidth()
            )
            TextField(
                value         = passwordConfirmation,
                onValueChange = { passwordConfirmation = it },
                placeholder   = { Text("confirmer le mot de passe") },
                singleLine    = true,
                trailingIcon  = {
                    IconButton(
                        onClick = { passwordConfirmationVisibility = !passwordConfirmationVisibility },
                        enabled = passwordConfirmation.isNotBlank(),
                    ) { Icon(
                        imageVector        = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Afficher le mot de passe",
                        modifier           = Modifier.size(24.dp)
                    )}
                },
                isError              = password != passwordConfirmation,
                visualTransformation = if (!passwordConfirmationVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                modifier             = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onNavigateToSignIn() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Se connecter",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.trySignUp(mailAddress, password, pin).fold(
                                onSuccess = { onSignUp() },
                                onFailure = {
                                    it.message?.let { message ->
                                        toastService.showToast(message)
                                    }
                                }
                            )
                        }
                    },
                    enabled = mailAddress.matches(emailRegex) && pin.length == 8 && pin.all { it.isDigit() } && password.isNotBlank() && password == passwordConfirmation,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "S'enregistrer",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}