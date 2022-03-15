package com.yt.graduation.UI.Settings


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.yt.graduation.model.User
import com.yt.graduation.repository.SettingsRepository
import com.yt.graduation.util.Resource

class SettingsViewModel: ViewModel() {
    private val repository = SettingsRepository()

    private val _user =  MutableLiveData<User>()
    val user : LiveData<User>
        get() =  _user

//    private val _userSignUpStatus = MutableLiveData<Boolean>()
//    val userSignUpStatus: LiveData<Boolean> = _userSignUpStatus

    fun setUser(user: User){
        if(user.name != ""){
            repository.setUser(user)
        }
    }

    fun getUser( callback: SettingsRepository.OnDataReceiveCallback):User{
       return repository.getUser(callback)
    }

    fun signOut(){
//        _userSignUpStatus.postValue(false)
        repository.signOut()
    }



}