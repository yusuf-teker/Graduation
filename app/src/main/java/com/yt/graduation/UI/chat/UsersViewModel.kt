package com.yt.graduation.UI.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yt.graduation.model.User
import com.yt.graduation.repository.UsersRepository

class UsersViewModel: ViewModel() {

    private var repository: UsersRepository = UsersRepository()

    private val _usersList = MutableLiveData<ArrayList<User>>()
    val usersList: LiveData<ArrayList<User>>
        get() = _usersList

    fun refreshSpeeches(){
//        repository.getSpeeches(callback = object :  UsersRepository.OnDataReceiveCallback<User>{
//            override fun onDataReceived(users: ArrayList<User>) {
//                _usersList.postValue(users)
//            }
//        })
        repository.getUsersWithContact(object :UsersRepository.OnDataReceiveCallback<User>{
            override fun onDataReceived(users: ArrayList<User>) {
                _usersList.postValue(users)
            }
        })

    }

}