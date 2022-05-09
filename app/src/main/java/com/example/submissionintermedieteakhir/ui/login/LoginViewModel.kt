package com.example.submissionintermedieteakhir.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.submissionintermedieteakhir.data.repository.UserRepo
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: UserRepo) : ViewModel() {
    fun login(email: String, password: String) = repo.login(email, password)
    fun setToken(token: String, isLogin: Boolean){
        viewModelScope.launch {
            repo.setToken(token, isLogin)
        }
    }

    fun getToken() : LiveData<String> {
        return repo.getToken().asLiveData()
    }
}