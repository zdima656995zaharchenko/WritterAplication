package com.example.writteraplication.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

sealed class ForgotPasswordUiState {
    object Idle : ForgotPasswordUiState()
    object Loading : ForgotPasswordUiState()
    data class Success(val message: String) : ForgotPasswordUiState()
    data class Error(val message: String) : ForgotPasswordUiState()
}

class ForgotPasswordViewModel : ViewModel() {

    var uiState by mutableStateOf<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
        private set

    fun resetPassword(email: String) {
        uiState = ForgotPasswordUiState.Loading

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                uiState = if (task.isSuccessful) {
                    ForgotPasswordUiState.Success("Якщо email зареєстрований, інструкції надіслано")
                } else {
                    ForgotPasswordUiState.Error(task.exception?.message ?: "Сталася помилка")
                }
            }
    }
}
