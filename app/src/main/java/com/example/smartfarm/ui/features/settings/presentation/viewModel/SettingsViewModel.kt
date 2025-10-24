package com.example.smartfarm.ui.features.settings.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.settings.domain.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadThemePreference()
    }

    private fun loadThemePreference() {
        viewModelScope.launch {
            themeRepository.getThemePreference().collect { theme ->
                _uiState.update { it.copy(theme = theme) }
            }
        }
    }

    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            themeRepository.setThemePreference(theme)
        }
    }
}

data class SettingsUiState(
    val theme: AppTheme = AppTheme.SYSTEM
)

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM
}