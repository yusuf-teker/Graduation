package com.yt.graduation.UI.Settings


import androidx.lifecycle.ViewModel
import com.yt.graduation.model.User
import com.yt.graduation.repository.SettingsRepository

class SettingsViewModel: ViewModel() {
    private val repository = SettingsRepository()
    val user_name = ""
    val user_image = ""


    fun setUser(user: User){
        if(user.image == ""){
            repository.setUser(user,false)
        }
        repository.setUser(user,true)

    }


    fun getUser( callback: SettingsRepository.OnDataReceiveCallback):User{
       return repository.getUser(callback)
    }



}