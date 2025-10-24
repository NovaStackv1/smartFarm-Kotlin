package com.example.smartfarm.ui.features.auth.data.repo

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.example.smartfarm.R
import com.example.smartfarm.ui.features.auth.data.local.UserPreferences
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    //@ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences
) {
    //private val credentialManager = CredentialManager.create(context)
    private fun getCredentialManager(context: Context): CredentialManager {
        return CredentialManager.create(context)
    }

    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser
//    val currentUser: StateFlow<FirebaseUser?> = firebaseAuth.currentUser
//        .map { user ->
//            Timber.d("AuthRepository: authStateChanged -> ${user?.uid ?: "null"}")
//            user
//        }
//        .stateIn(
//            scope = CoroutineScope(Dispatchers.IO),
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = firebaseAuth.currentUser
//        )
    val isLoggedIn = userPreferences.isLoggedIn

    /**
     * Sign in with Google using Credential Manager API (New Way - Not Deprecated)
     */
    suspend fun signInWithGoogle(context: Context): AuthResult {
        return try {
            // Step 1: Get credentials using Credential Manager
            val credentialResponse = getGoogleCredentials(context)

            // Step 2: Extract Google ID Token
            val googleIdToken = extractGoogleIdToken(credentialResponse)
                ?: return AuthResult.Error("Failed to extract Google ID token")

            // Step 3: Authenticate with Firebase
            authenticateWithFirebase(googleIdToken)

        } catch (e: GetCredentialException) {
            Timber.tag(TAG).e(e, "Credential Manager error")
            AuthResult.Error(handleCredentialException(e))
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Sign-in error")
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    /**
     * Get Google credentials using Credential Manager
     */
    private suspend fun getGoogleCredentials(context: Context): GetCredentialResponse {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false) // Show all accounts, not just authorized ones
            .setAutoSelectEnabled(true) // Auto-select if only one account
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return getCredentialManager(context).getCredential(
            request = request,
            context = context
        )
    }

    /**
     * Extract Google ID Token from credential response
     */
    private fun extractGoogleIdToken(response: GetCredentialResponse): String? {
        val credential = response.credential

        return if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            googleIdTokenCredential.idToken
        } else {
            null
        }
    }

    /**
     * Authenticate with Firebase using Google ID token
     */
    private suspend fun authenticateWithFirebase(idToken: String): AuthResult {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user

            if (user != null) {
                // Save user session
                userPreferences.saveUserSession(
                    userId = user.uid,
                    email = user.email ?: "",
                    name = user.displayName ?: ""
                )
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Authentication failed: User is null")
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Firebase auth error")
            AuthResult.Error(e.message ?: "Firebase authentication failed")
        }
    }

    /**
     * Sign out from Firebase and clear credentials
     */
    suspend fun signOut(context: Context) {
        try {
            // Sign out from Firebase
            firebaseAuth.signOut()

            // Clear user preferences
            userPreferences.clearUserSession()

            // Clear credential state from Credential Manager
            val clearRequest = ClearCredentialStateRequest()
            getCredentialManager(context).clearCredentialState(clearRequest)

            Timber.tag(TAG).d("Sign out successful")
        } catch (e: ClearCredentialException) {
            Timber.tag(TAG).e(e, "Error clearing credentials")
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Sign out error")
        }
    }

    /**
     * Handle Credential Manager exceptions
     */
    private fun handleCredentialException(e: GetCredentialException): String {
        return when (e::class.simpleName) {
            "GetCredentialCancellationException" -> "Sign-in was cancelled"
            "GetCredentialInterruptedException" -> "Sign-in was interrupted"
            "NoCredentialException" -> "No Google accounts found. Please add a Google account."
            "GetCredentialUnknownException" -> "An unknown error occurred"
            else -> e.message ?: "Failed to sign in with Google"
        }
    }

    companion object {
        private const val TAG = "AuthRepository"
    }
}