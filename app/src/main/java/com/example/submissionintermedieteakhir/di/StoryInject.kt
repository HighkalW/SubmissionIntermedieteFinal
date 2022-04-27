package com.example.submissionintermedieteakhir.di

import android.content.Context
import com.example.submissionintermedieteakhir.data.local.room.StoryDatabase
import com.example.submissionintermedieteakhir.data.remote.retrofit.ApiConfig
import com.example.submissionintermedieteakhir.data.repository.StoryRepo

object StoryInject {
    fun provideRepository(context: Context): StoryRepo {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepo(apiService, database)
    }
}