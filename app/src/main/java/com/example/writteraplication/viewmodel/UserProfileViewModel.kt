
package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

    init {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                try {
                    _userName.value = authRepository.getUserName(userId)
                    _email.value = FirebaseAuth.getInstance().currentUser?.email
                } catch (e: Exception) {
                    // Логування або обробка помилки
                    _userName.value = "Помилка"
                    _email.value = "Невідомо"
                }
            }
        }
    }
}
