package com.example.submissionintermedieteakhir.ui.map

import androidx.lifecycle.ViewModel
import com.example.submissionintermedieteakhir.data.repository.StoryRepo


class MapsViewModel(private val storyRepo: StoryRepo) : ViewModel() {
    fun getStories(token: String) = storyRepo.getStoryLocation(token)
}