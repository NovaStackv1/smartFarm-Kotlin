package com.example.smartfarm.ui.features.auth.viewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.auth.data.repo.AuthRepository
import com.example.smartfarm.ui.features.auth.data.repo.AuthResult
import com.example.smartfarm.ui.features.auth.domain.model.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
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

    /**
     * Check if user is already logged in on app start
     */
    private fun checkAuthStatus() {
        viewModelScope.launch {
            authRepository.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn && authRepository.currentUser != null) {
                    Timber.tag(TAG).d("User already logged in, navigating to dashboard")
                    _authState.value = AuthState.Authenticated
                    _navigationEvent.emit(NavigationEvent.NavigateToDashboard)
                }
            }
        }
    }

    /**
     * Sign in with Google using Credential Manager
     */
    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            when (val result = authRepository.signInWithGoogle(context)) {
                is AuthResult.Success -> {
                    Timber.tag(TAG).d("Sign-in successful: ${result.user.email}")
                    _authState.value = AuthState.Authenticated
                    _navigationEvent.emit(NavigationEvent.NavigateToDashboard)
                }
                is AuthResult.Error -> {
                    Timber.tag(TAG).e("Sign-in failed: ${result.message}")
                    _authState.value = AuthState.Error(result.message)
                }
            }
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Idle
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}

sealed class NavigationEvent {
    data object NavigateToDashboard : NavigationEvent()
}