package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.writteraplication.viewmodel.ChangePasswordUiState
import com.example.writteraplication.viewmodel.ChangePasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onBackToProfile: () -> Unit,
    viewModel: ChangePasswordViewModel = viewModel()
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val uiState by remember { viewModel::uiState }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Зміна пароля") },
                navigationIcon = {
                    IconButton(onClick = onBackToProfile) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Поточний пароль
            Text(
                text = "Введіть поточний пароль",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text("Поточний пароль") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Новий пароль
            Text(
                text = "Введіть новий пароль",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Новий пароль") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Підтвердження пароля") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (newPassword.isNotBlank() && newPassword == confirmPassword) {
                        viewModel.updatePassword(currentPassword, newPassword)
                    } else {
                        viewModel.setError("Паролі не співпадають")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is ChangePasswordUiState.Loading
            ) {
                Text("Змінити пароль")
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (val state = uiState) {
                is ChangePasswordUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is ChangePasswordUiState.Success -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                is ChangePasswordUiState.Error -> {
                    Text(
                        text = "Oh, something's off: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                else -> {}
            }
        }
    }
}



