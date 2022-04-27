package com.example.submissionintermedieteakhir.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionintermedieteakhir.data.repository.UserRepo
import com.example.submissionintermedieteakhir.di.UserInject
import com.example.submissionintermedieteakhir.ui.login.LoginViewModel
import com.example.submissionintermedieteakhir.ui.signup.SignupViewModel

class UserVMF (private val userRepo: UserRepo) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepo) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: UserVMF? = null
        fun getInstance(context: Context): UserVMF =
            instance ?: synchronized(this) {
                instance ?: UserVMF(UserInject.provideRepository(context))
            }.also { instance = it }
    }
}