package com.example.submissionintermedieteakhir.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.submissionintermedieteakhir.data.remote.retrofit.ApiConfig
import com.example.submissionintermedieteakhir.data.repository.UserRepo

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object UserInject {
    fun provideRepository(context: Context): UserRepo {
        val apiService = ApiConfig.getApiService()
        return UserRepo.getInstance(context.dataStore, apiService)
    }
}