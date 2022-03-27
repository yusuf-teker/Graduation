package com.yt.graduation.UI.Authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.yt.graduation.model.User
import com.yt.graduation.repository.RegisterRepository
import com.yt.graduation.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {



    private var _userRegistrationStatus = MutableLiveData<Resource<AuthResult>>() //AuthResult Firebase'e ait user infosunu içerir
    val userRegistrationStatus: LiveData<Resource<AuthResult>>
        get() = _userRegistrationStatus

    private val repository = RegisterRepository()

    private fun validateRegistration(name: String, email: String, password1:String, password2: String):Boolean{
        return email.takeLast(11)=="@itu.edu.tr"  && name.length in 2..25 && password1.isNotEmpty() && password1 == password2
    }

    fun register(user: User,password1:String,password2:String){
        _userRegistrationStatus.postValue(Resource.Loading())
        if (validateRegistration(user.name,user.email,password1,password2)){
            viewModelScope.launch(Dispatchers.Main) {
                val registerResult =  repository.register(user,password1)
                _userRegistrationStatus.postValue(registerResult)//_userRegistrationStatus.value = registerResult

            }
        }else _userRegistrationStatus.postValue(Resource.Error("Register failed."))
    }
}