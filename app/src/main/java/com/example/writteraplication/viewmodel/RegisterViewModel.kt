
package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            val result = authRepository.registerUser(email, password)
            if (result.isSuccess) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                userId?.let {
                    authRepository.saveUserName(it, name)
                }
                _registrationState.value = RegistrationState.Success
            } else {
                _registrationState.value = RegistrationState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}
