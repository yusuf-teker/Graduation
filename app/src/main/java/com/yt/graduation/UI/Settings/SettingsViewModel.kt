package com.yt.graduation.UI.Settings


import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yt.graduation.model.User
import com.yt.graduation.repository.SettingsRepository
import com.yt.graduation.util.FirebaseResultListener
import com.yt.graduation.util.OnDataReceiveCallback

class SettingsViewModel: ViewModel() {



    private val _updatingStatus = MutableLiveData<Boolean>()
    val updatingStatus : LiveData<Boolean>
        get() = _updatingStatus

    private val repository = SettingsRepository()

    fun setUser(user: User){
        _updatingStatus.postValue(true)
        if(user.name != ""){
            repository.setUser(user, resultListener = object : FirebaseResultListener {
                override fun onSuccess(isSuccess: Boolean) {
                   _updatingStatus.postValue(false)
                }
            })
        }
    }

    fun getUser( callback: OnDataReceiveCallback):User{
       return repository.getUser(callback)
    }

    fun signOut(){
        repository.signOut()
    }



}