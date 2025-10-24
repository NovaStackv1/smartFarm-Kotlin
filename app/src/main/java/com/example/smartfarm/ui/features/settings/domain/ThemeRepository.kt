
package com.example.smartfarm.ui.features.settings.domain

import com.example.smartfarm.ui.features.settings.presentation.viewModel.AppTheme
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getThemePreference(): Flow<AppTheme>
    suspend fun setThemePreference(theme: AppTheme)
}