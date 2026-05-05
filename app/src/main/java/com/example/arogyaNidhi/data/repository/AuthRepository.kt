package com.example.arogyaNidhi.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.arogyaNidhi.data.models.AppUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "arogya_prefs")

class AuthRepository(private val context: Context) {

    private val gson = Gson()

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val CURRENT_USER = stringPreferencesKey("current_user")
        val USERS_LIST = stringPreferencesKey("users_list")
    }

    suspend fun isLoggedIn(): Boolean {
        return context.dataStore.data.map { prefs ->
            prefs[IS_LOGGED_IN] ?: false
        }.first()
    }

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<AppUser> {
        val users = getAllUsers()
        if (users.any { it.email == email }) {
            return Result.failure(Exception("Email already registered. Please login."))
        }
        val newUser = AppUser(username, email, password)
        val updatedUsers = users + newUser
        context.dataStore.edit { prefs ->
            prefs[USERS_LIST] = gson.toJson(updatedUsers)
            prefs[IS_LOGGED_IN] = true
            prefs[CURRENT_USER] = gson.toJson(newUser)
        }
        return Result.success(newUser)
    }

    suspend fun login(email: String, password: String): Result<AppUser> {
        val users = getAllUsers()
        val user = users.find { it.email == email && it.password == password }
            ?: return Result.failure(Exception("Invalid email or password"))
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = true
            prefs[CURRENT_USER] = gson.toJson(user)
        }
        return Result.success(user)
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = false
            prefs.remove(CURRENT_USER)
        }
    }

    suspend fun getCurrentUser(): AppUser? {
        val userJson = context.dataStore.data.map { prefs ->
            prefs[CURRENT_USER]
        }.first() ?: return null
        return try {
            gson.fromJson(userJson, AppUser::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun getAllUsers(): List<AppUser> {
        val json = context.dataStore.data.map { prefs ->
            prefs[USERS_LIST]
        }.first() ?: return emptyList()
        return try {
            val type = object : TypeToken<List<AppUser>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}