package com.yt.graduation.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

//Tip: Create the DataStore instance at the top level of your Kotlin file once, and access it through this property throughout the rest of your application. This makes it easier to keep your DataStore as a singleton.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(context: Context) {

    private val wallpaper = stringPreferencesKey("wallpaper")

    val preferenceFlow: Flow<String> = context.dataStore.data //data Flow<T>
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[wallpaper] ?: "default"
        }

    suspend fun saveLayoutToPreferencesStore(wallpaperString: String, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[wallpaper] = wallpaperString
        }
    }
}