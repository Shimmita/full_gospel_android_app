package com.shimmita.full_gospel.auth

import User
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")


class DataStoreManager(private val context: Context) {
    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val TEMP_KEY = stringPreferencesKey("temp_key")
        private val USER_PROFILE = stringPreferencesKey("user_profile")

    }

    private val gson = Gson()

    val userProfile: Flow<User?> =
        context.dataStore.data.map { prefs ->
            prefs[USER_PROFILE]?.let {
                gson.fromJson(it, User::class.java)
            }
        }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = true
            prefs[USER_PROFILE] = gson.toJson(user)
        }
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[IS_LOGGED_IN] ?: false
    }

    val tempKeyValue: Flow<String?> =
        context.dataStore.data.map { it[TEMP_KEY] }

    suspend fun setLoggedIn(loginValue: Boolean, tempKey: String) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = loginValue
            prefs[TEMP_KEY] = tempKey
        }
    }



    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
