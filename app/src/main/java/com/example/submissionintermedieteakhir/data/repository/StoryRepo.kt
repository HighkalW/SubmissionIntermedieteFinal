package com.example.submissionintermedieteakhir.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.submissionintermedieteakhir.data.StoryRemoteMedia
import com.example.submissionintermedieteakhir.data.local.entity.Story
import com.example.submissionintermedieteakhir.data.local.room.StoryDatabase
import com.example.submissionintermedieteakhir.data.remote.response.StoryResponse
import com.example.submissionintermedieteakhir.data.remote.response.UploadResponse
import com.example.submissionintermedieteakhir.data.remote.retrofit.ApiService
import com.example.submissionintermedieteakhir.utils.wrapEspressoIdlingResource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import com.example.submissionintermedieteakhir.data.result.Result


class StoryRepo (private val apiService: ApiService, private val storyDatabase: StoryDatabase){

    fun uploadStory(token: String, imageMultipart: MultipartBody.Part, desc: RequestBody, lat: RequestBody?, lon: RequestBody?): LiveData<Result<UploadResponse>> = liveData{
        emit(Result.Loading)
        try {
            val client = apiService.uploadStory("Bearer $token",imageMultipart, desc, lat, lon)
            emit(Result.Success(client))
        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoryLocation(token: String) : LiveData<Result<StoryResponse>> = liveData{
        emit(Result.Loading)
        try {
            val client = apiService.getStories("Bearer $token", location = 1)
            emit(Result.Success(client))
        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStories(token: String): LiveData<PagingData<Story>> {
        wrapEspressoIdlingResource {
            @OptIn(ExperimentalPagingApi::class)
            return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                remoteMediator = StoryRemoteMedia(storyDatabase, apiService, token),
                pagingSourceFactory = {
                    storyDatabase.storyDao().getAllStories()
                }
            ).liveData
        }
    }
}