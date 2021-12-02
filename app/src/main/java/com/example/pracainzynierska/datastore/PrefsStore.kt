package com.example.pracainzynierska.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore("user")

class PrefsStore @Inject constructor(
    @ApplicationContext appContext: Context
) {
    private val userDataStore = appContext.dataStore
    val SESSION_TOKEN = stringPreferencesKey("session_token")


    suspend fun setToken(token: String) {
        userDataStore.edit { settings ->
            Log.d("tokenlog", "settoken: $token")

            settings[SESSION_TOKEN] = token
        }
    }

    val getToken: Flow<String> = userDataStore.data
        .map { preferences ->
            Log.d("tokenlog", "gettoken: ${preferences[SESSION_TOKEN]}")
        preferences[SESSION_TOKEN] ?: "undefined"
    }






}