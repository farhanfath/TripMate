package gli.project.tripmate.presentation.ui.screen.auth.register

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Lock
import compose.icons.tablericons.Mail
import gli.project.tripmate.presentation.ui.component.auth.CustomInputField
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.component.common.CustomTopBar
import gli.project.tripmate.presentation.util.extensions.HandlerResponseCompose
import gli.project.tripmate.presentation.util.iconUrl
import gli.project.tripmate.presentation.viewmodel.auth.UserViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

    // viewmodel
    val viewModel: UserViewModel = hiltViewModel()
    val authState = viewModel.authState.collectAsState()

    val enableStatus = !authState.value.onProcess

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            CustomTopBar()
        }
        // back button
        item {
            IconButton(
                onClick = {
                    onBackClick()
                },
                modifier = Modifier
                    .padding(top = 32.dp, start = 24.dp)
                    .border(
                        shape = RoundedCornerShape(16.dp),
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(0.7f)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "back"
                )
            }
        }

        // header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomImageLoader(
                    url = iconUrl,
                    desc = "Duck Logo",
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .size(80.dp)
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    textAlign = TextAlign.Start,
                    text = "Register",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Start,
                    text = "Daftar sekarang dan mulai pantau peternakan Anda.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        // login form
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 24.dp)
            ) {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                CustomInputField(
                    valueState = email,
                    placeHolder = "Masukkan Email Anda",
                    enabled = enableStatus,
                    leadingIcon = TablerIcons.Mail,
                )
                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    text = "Password",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                CustomInputField(
                    valueState = password,
                    placeHolder = "Masukkan Password",
                    enabled = enableStatus,
                    leadingIcon = TablerIcons.Lock,
                    trailingIconAction = {
                        val customIcon = if (passwordVisible.value) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        }
                        IconButton(
                            modifier = Modifier.padding(end = 10.dp),
                            onClick = { passwordVisible.value = !passwordVisible.value }
                        ) {
                            Icon(
                                imageVector = customIcon,
                                contentDescription = "password visibility",
                                tint = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    },
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.size(20.dp))
            }
        }

        // auth function
        item {
            val context = LocalContext.current
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 15.dp,
                        disabledElevation = 0.dp
                    ),
                    enabled = enableStatus,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        when {
                            email.value.isEmpty() -> {
                                Toast.makeText(context, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                            }
                            password.value.isEmpty() -> {
                                Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                viewModel.userRegister(
                                    email = email.value,
                                    password = password.value
                                )
                            }
                        }
                    }
                ) {
                    HandlerResponseCompose(
                        response = authState.value.registerStatus,
                        onInitiate = {
                            Text(
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                text = "Register",
                            )
                        },
                        onLoading = {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        },
                        onSuccess = {
                            onRegisterSuccess()
                            Toast.makeText(context, "Register berhasil", Toast.LENGTH_SHORT).show()
                            viewModel.resetAllAuthStatus()
                        },
                        onError = { err ->
                            Toast.makeText(context, err.message, Toast.LENGTH_SHORT).show()
                            viewModel.resetAllAuthStatus()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview(modifier: Modifier = Modifier) {
    RegisterScreen(
        onBackClick = {},
        onRegisterSuccess = {}
    )
}