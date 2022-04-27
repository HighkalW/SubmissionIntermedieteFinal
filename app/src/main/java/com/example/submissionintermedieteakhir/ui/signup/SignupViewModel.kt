package com.example.submissionintermedieteakhir.ui.signup

import androidx.lifecycle.ViewModel
import com.example.submissionintermedieteakhir.data.repository.UserRepo

class SignupViewModel(private val repo: UserRepo) : ViewModel() {

    fun register(name: String, email: String, password: String) = repo.register(name, email, password)
}