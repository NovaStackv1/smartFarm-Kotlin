package com.example.smartfarm.ui.features.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.auth.data.repo.AuthRepository
import com.example.smartfarm.ui.features.auth.data.repo.AuthResult
import com.example.smartfarm.ui.features.auth.domain.model.AuthState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            authRepository.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn && authRepository.currentUser != null) {
                    _authState.value = AuthState.Authenticated
                    _navigationEvent.emit(NavigationEvent.NavigateToDashboard)
                }
            }
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            when (val result = authRepository.signInWithGoogle(account)) {
                is AuthResult.Success -> {
                    _authState.value = AuthState.Authenticated
                    _navigationEvent.emit(NavigationEvent.NavigateToDashboard)
                }
                is AuthResult.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
            }
        }
    }

    fun clearError() {
        _authState.value = AuthState.Idle
    }
}

sealed class NavigationEvent {
    data object NavigateToDashboard : NavigationEvent()
}