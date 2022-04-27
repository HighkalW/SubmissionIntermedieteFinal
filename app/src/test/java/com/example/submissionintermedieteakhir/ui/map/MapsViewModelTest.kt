package com.example.submissionintermedieteakhir.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.submissionintermedieteakhir.data.dummy.DataDummy
import com.example.submissionintermedieteakhir.data.result.Result
import com.example.submissionintermedieteakhir.data.remote.response.StoryResponse
import com.example.submissionintermedieteakhir.data.repository.StoryRepo
import com.example.submissionintermedieteakhir.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepo
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyToken = "azhfxrdjgchfgchjvjhfhdgcvcnv"
    private val dummyStory = DataDummy.generateDummyStoryResponse()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when Network error Should Return Error`() {
        val expectedStories = MutableLiveData<Result<StoryResponse>>()
        expectedStories.value = Result.Error("Error")
        Mockito.`when`(mapsViewModel.getStories(dummyToken)).thenReturn(expectedStories)

        val actualStories = mapsViewModel.getStories(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryLocation(dummyToken)
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is Result.Error)
    }

    @Test
    fun `when Get maps story Should Not Null and Return Success`() {
        val expectedStories = MutableLiveData<Result<StoryResponse>>()
        expectedStories.value = Result.Success(dummyStory)
        Mockito.`when`(mapsViewModel.getStories(dummyToken)).thenReturn(expectedStories)

        val actualStories = mapsViewModel.getStories(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryLocation(dummyToken)
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is Result.Success)
        Assert.assertSame(dummyStory, (actualStories as Result.Success).data)
        Assert.assertEquals(dummyStory.listStory.size, actualStories.data.listStory.size)
    }
}