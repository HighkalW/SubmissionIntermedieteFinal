package com.example.submissionintermedieteakhir.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.submissionintermedieteakhir.CoroutineRules
import com.example.submissionintermedieteakhir.data.dummy.DataDummy
import com.example.submissionintermedieteakhir.data.remote.response.LoginResponse
import com.example.submissionintermedieteakhir.data.repository.UserRepo
import com.example.submissionintermedieteakhir.data.result.Result
import com.example.submissionintermedieteakhir.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepo
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyToken = "azhfxrdjgchfgchjvjhfhdgcvcnv"
    private val dummyEmail = "user@email.com"
    private val dummyPassword = "password"

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(userRepository)
    }

    @get:Rule
    var mainCoroutineRule = CoroutineRules()

    @Test
    fun `when signup failed and Result Error`() {
        val loginResponse = MutableLiveData<Result<LoginResponse>>()
        loginResponse.value = Result.Error("Error")

        Mockito.`when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(loginResponse)

        val actualLoginResponse = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(userRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualLoginResponse)
        Assert.assertTrue(actualLoginResponse is Result.Error)
    }

    @Test
    fun `when login success and Result Success`() {
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Success(dummyLoginResponse)

        Mockito.`when`(loginViewModel.login(dummyEmail, dummyPassword))
            .thenReturn(expectedLoginResponse)

        val actualLoginResponse = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(userRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualLoginResponse)
        Assert.assertTrue(actualLoginResponse is Result.Success)
        Assert.assertSame(dummyLoginResponse, (actualLoginResponse as Result.Success).data)
    }

    @Test
    fun `Save token successfully`() = mainCoroutineRule.runBlockingTest {
        loginViewModel.setToken(dummyToken, true)
        Mockito.verify(userRepository).setToken(dummyToken, true)
    }
}