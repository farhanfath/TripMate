package gli.project.tripmate.presentation.ui.screen.auth.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import gli.project.tripmate.R
import gli.project.tripmate.presentation.ui.component.auth.CustomInputField
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.component.common.CustomTopBar
import gli.project.tripmate.presentation.util.extensions.HandlerResponseCompose
import gli.project.tripmate.presentation.util.iconUrl
import gli.project.tripmate.presentation.viewmodel.auth.UserViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onBackClick: () ->  Unit,
    onForgotPass: () -> Unit = {},
    onNavigateRegister: () ->  Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

    val viewModel: UserViewModel = hiltViewModel()
    val authState = viewModel.authState.collectAsState()

    val enableStatus = !authState.value.onProcess

    Scaffold(
        topBar = {
            CustomTopBar()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.imePadding()
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            textAlign = TextAlign.Start,
                            text = "Login",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            textAlign = TextAlign.Start,
                            text = "Silahkan Login Untuk melanjutkan",
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
                        /**
                         * //TODO: forgot password feature coming soon
                         */
                        TextButton(
                            modifier = Modifier
                                .align(Alignment.End),
                            onClick = {
                                onForgotPass()
                            }
                        ) {
                            Text(
                                textAlign = TextAlign.End,
                                text = "Lupa Password?",
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
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
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(),
                            onClick = {
                                when {
                                    email.value.isEmpty() -> {
                                        Toast.makeText(context, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                                    }
                                    password.value.isEmpty() -> {
                                        Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        viewModel.userLogin(
                                            email = email.value,
                                            password = password.value
                                        )
                                    }
                                }
                            }
                        ) {
                            HandlerResponseCompose(
                                response = authState.value.loginStatus,
                                onInitiate = {
                                    Text(
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        text = "Login",
                                    )
                                },
                                onLoading = {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                },
                                onSuccess = {
                                    onLoginSuccess()
                                    Toast.makeText(context, "Login berhasil", Toast.LENGTH_SHORT).show()
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

                // Item untuk teks "Baru Bergabung?"
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Baru Bergabung? ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "Daftar Sekarang",
                            modifier = Modifier.clickable {
                                onNavigateRegister()
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview(modifier: Modifier = Modifier) {
    LoginScreen(
        onLoginSuccess = {},
        onNavigateRegister = {},
        onBackClick = {},
        onForgotPass = {}
    )
}