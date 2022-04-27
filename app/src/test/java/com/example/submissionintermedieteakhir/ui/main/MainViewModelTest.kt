package com.example.submissionintermedieteakhir.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.submissionintermedieteakhir.CoroutineRules
import com.example.submissionintermedieteakhir.PaggingDataSourceTest
import com.example.submissionintermedieteakhir.adapter.ListStoryAdapter
import com.example.submissionintermedieteakhir.data.dummy.DataDummy
import com.example.submissionintermedieteakhir.data.local.entity.Story
import com.example.submissionintermedieteakhir.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = CoroutineRules()

    @Mock
    private lateinit var mainViewModel: MainViewModel
    private val dummyToken = "azhfxrdjgchfgchjvjhfhdgcvcnv"

    @Test
    fun `set logout successfully`() = mainCoroutineRule.runBlockingTest {
        mainViewModel.logout()
        Mockito.verify(mainViewModel).logout()
    }

    @Test
    fun `get token successfully`() {
        val expectedToken = MutableLiveData<String>()
        expectedToken.value = dummyToken
        Mockito.`when`(mainViewModel.getToken()).thenReturn(expectedToken)

        val actualToken = mainViewModel.getToken().getOrAwaitValue()
        Mockito.verify(mainViewModel).getToken()
        Assert.assertNotNull(actualToken)
        Assert.assertEquals(dummyToken, actualToken)
    }

    @Test
    fun `when get list story should not sull`() = mainCoroutineRule.runBlockingTest {
        val dummyStories = DataDummy.generateDummyStoryList()
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = PaggingDataSourceTest.snapshot(dummyStories)
        Mockito.`when`(mainViewModel.getStories(dummyToken)).thenReturn(expectedStories)
        val actualStories = mainViewModel.getStories(dummyToken).getOrAwaitValue()

        val noopListUpdateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {}
        }
        val storyDiffer = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher,
        )
        storyDiffer.submitData(actualStories)

        advanceUntilIdle()

        Mockito.verify(mainViewModel).getStories(dummyToken)
        Assert.assertNotNull(actualStories)
        Assert.assertEquals(dummyStories.size, storyDiffer.snapshot().size)
    }

    @Test
    fun `get session login successfully`() {
        val dummySession = true
        val expectedSession = MutableLiveData<Boolean>()
        expectedSession.value = dummySession
        Mockito.`when`(mainViewModel.isLogin()).thenReturn(expectedSession)

        val actualSession = mainViewModel.isLogin().getOrAwaitValue()
        Mockito.verify(mainViewModel).isLogin()
        Assert.assertNotNull(actualSession)
        Assert.assertEquals(dummySession, actualSession)
    }
}