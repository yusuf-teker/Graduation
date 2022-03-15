package com.yt.graduation.UI.Authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.yt.graduation.repository.LoginRepository
import com.yt.graduation.util.Resource
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private var repository = LoginRepository()

    private val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignUpStatus: LiveData<Resource<AuthResult>> = _userSignUpStatus

    fun login(email: String, password: String){
        _userSignUpStatus.postValue(Resource.Loading())
        viewModelScope.launch {
            val loginResult = repository.login(email,password)
            _userSignUpStatus.postValue(loginResult) //repo'dan gelen Resultı viewModel içinde değiştir
        }
    }
}