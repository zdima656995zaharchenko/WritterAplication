
package com.example.writteraplication.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.writteraplication.viewmodel.RegisterViewModel
import com.example.writteraplication.viewmodel.RegistrationState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onBackToMainMenu: () -> Unit,
    onRegistrationSuccess: () -> Unit = {}
) {
    val viewModel: RegisterViewModel = viewModel()
    val registrationState by viewModel.registrationState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(registrationState) {
        when (registrationState) {
            is RegistrationState.Success -> onRegistrationSuccess()
            is RegistrationState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        (registrationState as RegistrationState.Error).message
                    )
                }
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("До Головного Меню") },
                navigationIcon = {
                    IconButton(onClick = { onBackToMainMenu() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Реєстрація", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Ім’я") })
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        viewModel.register(name.trim(), email.trim(), password)
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Невірний формат email")
                        }
                    }
                },
                enabled = registrationState !is RegistrationState.Loading
            ) {
                Text("Зареєструватися")
            }

            if (registrationState is RegistrationState.Loading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}
