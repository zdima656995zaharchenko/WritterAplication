package com.example.writteraplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

sealed class ChangePasswordUiState {
    object Idle : ChangePasswordUiState()
    object Loading : ChangePasswordUiState()
    data class Success(val message: String) : ChangePasswordUiState()
    data class Error(val message: String) : ChangePasswordUiState()
}

class ChangePasswordViewModel : ViewModel() {

    // Приватне поле зі станом
    private var _uiState by mutableStateOf<ChangePasswordUiState>(ChangePasswordUiState.Idle)
    val uiState: ChangePasswordUiState
        get() = _uiState

    fun updatePassword(currentPassword: String, newPassword: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        if (user == null || email.isNullOrBlank()) {
            _uiState = ChangePasswordUiState.Error("Користувач не авторизований або email відсутній")
            return
        }

        _uiState = ChangePasswordUiState.Loading

        val credential = EmailAuthProvider.getCredential(email, currentPassword)

        user.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            _uiState = if (updateTask.isSuccessful) {
                                ChangePasswordUiState.Success("Пароль успішно змінено")
                            } else {
                                ChangePasswordUiState.Error(updateTask.exception?.message ?: "Помилка при зміні пароля")
                            }
                        }
                } else {
                    _uiState = ChangePasswordUiState.Error(
                        reauthTask.exception?.message ?: "Помилка повторної автентифікації"
                    )
                }
            }
    }

    fun setError(message: String) {
        _uiState = ChangePasswordUiState.Error(message)
    }
}
