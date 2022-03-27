package com.yt.graduation.UI.Settings


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yt.graduation.model.User
import com.yt.graduation.repository.SettingsRepository
import com.yt.graduation.util.FirebaseResultListener
import com.yt.graduation.util.OnDataReceiveCallback

class SettingsViewModel: ViewModel() {

    private val repository = SettingsRepository()

    fun setUser(user: User,resultListener: FirebaseResultListener){
        if(user.name != ""){
            repository.setUser(user,resultListener)
        }
    }

    fun getUser( callback: OnDataReceiveCallback):User{
       return repository.getUser(callback)
    }

    fun signOut(){
        repository.signOut()
    }



}