package com.example.smartfarm.ui.features.settings.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.smartfarm.ui.features.settings.domain.ThemeRepository
import com.example.smartfarm.ui.features.settings.presentation.viewModel.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ThemeRepository {

    private object PreferencesKeys {
        val THEME = stringPreferencesKey("app_theme")
    }

    override fun getThemePreference(): Flow<AppTheme> {
        return dataStore.data.map { preferences ->
            val themeString = preferences[PreferencesKeys.THEME] ?: AppTheme.SYSTEM.name
            try {
                AppTheme.valueOf(themeString)
            } catch (e: IllegalArgumentException) {
                AppTheme.SYSTEM
            }
        }
    }

    override suspend fun setThemePreference(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme.name
        }
    }
}