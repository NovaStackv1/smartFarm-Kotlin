package com.example.smartfarm.ui.features.profile.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class UserData(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val profilePictureUrl: String? = null,
    val joinDate: String? = null
)

data class ProfileState(
    val userData: UserData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchUserData() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
                    
                    val userData = UserData(
                        name = userDoc.getString("name") ?: currentUser.displayName ?: "Smart Farmer",
                        email = currentUser.email ?: "No email",
                        phone = userDoc.getString("phone"),
                        profilePictureUrl = userDoc.getString("profilePictureUrl") ?: currentUser.photoUrl?.toString(),
                        joinDate = userDoc.getTimestamp("createdAt")?.toDate()?.toString() ?: "Recently"
                    )
                    
                    _uiState.value = _uiState.value.copy(
                        userData = userData,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "No user logged in",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load profile: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                auth.signOut()
                // Navigation handled by the composable
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Logout failed: ${e.message}"
                )
            }
        }
    }
}