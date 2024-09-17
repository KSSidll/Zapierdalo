package com.kssidll.zapierdalo.domain.preference

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.kssidll.zapierdalo.R
import com.kssidll.zapierdalo.di.module.getPreferencesDataStore
import kotlinx.coroutines.flow.map

data object AppPreferences {
    const val DATASTORENAME: String = "settings"

    data object Theme {
        data object ColorScheme {
            val key: Preferences.Key<Int> = intPreferencesKey("themecolorscheme")

            val DEFAULT = Values.SYSTEM

            enum class Values {
                SYSTEM,
                DARK,
                LIGHT

                ;

                @Composable
                @ReadOnlyComposable
                fun getTranslation(): String {
                    return when (this) {
                        SYSTEM -> stringResource(R.string.system)
                        DARK -> stringResource(R.string.dark)
                        LIGHT -> stringResource(R.string.light)
                    }
                }

                companion object {
                    fun get(index: Int?) =
                        index?.let { Values.entries.getOrElse(index) { DEFAULT } } ?: DEFAULT
                }
            }
        }

        data object DynamicColor {
            @RequiresApi(Build.VERSION_CODES.S)
            val key: Preferences.Key<Boolean> = booleanPreferencesKey("themedynamiccolor")

            val DEFAULT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        }
    }

    fun getColorScheme(context: Context) =
        getPreferencesDataStore(context).data.map { preferences ->
            Theme.ColorScheme.Values.get(preferences[Theme.ColorScheme.key])
        }

    suspend fun setColorScheme(
        context: Context,
        newColorScheme: Theme.ColorScheme.Values
    ) {
        getPreferencesDataStore(context).edit {
            it[Theme.ColorScheme.key] = newColorScheme.ordinal
        }
    }

    fun getDynamicColor(context: Context) =
        getPreferencesDataStore(context).data.map { preferences ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                preferences[Theme.DynamicColor.key] ?: Theme.DynamicColor.DEFAULT
            } else Theme.DynamicColor.DEFAULT
        }

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun setDynamicColor(
        context: Context,
        newDynamicColor: Boolean
    ) {
        getPreferencesDataStore(context).edit {
            it[Theme.DynamicColor.key] = newDynamicColor
        }
    }
}

fun AppPreferences.Theme.ColorScheme.Values.detectDarkMode(): (Resources) -> Boolean =
    { resources ->
        when (this) {
            AppPreferences.Theme.ColorScheme.Values.SYSTEM -> (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            AppPreferences.Theme.ColorScheme.Values.DARK -> true
            AppPreferences.Theme.ColorScheme.Values.LIGHT -> false
        }
    }
