package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.writteraplication.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onChangePasswordClick: () -> Unit, // Додано
    onLoginSuccess: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState by loginViewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (loginState) {
            is LoginViewModel.LoginState.Error -> {
                Text(
                    text = (loginState as LoginViewModel.LoginState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            is LoginViewModel.LoginState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }
            is LoginViewModel.LoginState.Success -> {
                LaunchedEffect(Unit) {
                    onLoginSuccess()
                }
            }
            else -> {}
        }

        TextButton(onClick = onForgotPasswordClick) {
            Text("Забули пароль?")
        }

        TextButton(onClick = onChangePasswordClick) { // Додано
            Text("Змінити пароль")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                loginViewModel.login(email, password)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is LoginViewModel.LoginState.Loading
        ) {
            Text("Увійти")
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onRegisterClick) {
            Text("Не маєте акаунту? Зареєструватися")
        }
    }
}
