package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.writteraplication.viewmodel.ForgotPasswordUiState
import com.example.writteraplication.viewmodel.ForgotPasswordViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    val uiState by remember { viewModel::uiState }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Скидання паролю") },
                navigationIcon = {
                    IconButton(onClick = onBackToLogin) {
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
            Text("Введіть свій email, щоб отримати інструкції", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        viewModel.resetPassword(email)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is ForgotPasswordUiState.Loading
            ) {
                Text("Надіслати інструкції")
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (val state = uiState) {
                is ForgotPasswordUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is ForgotPasswordUiState.Success -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                is ForgotPasswordUiState.Error -> {
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
