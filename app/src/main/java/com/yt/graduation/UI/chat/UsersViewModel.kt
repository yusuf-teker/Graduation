package com.yt.graduation.UI.chat

import android.app.Application
import androidx.lifecycle.*
import com.yt.graduation.data.SettingsDataStore
import com.yt.graduation.model.User
import com.yt.graduation.repository.UsersRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UsersViewModel(application: Application)   : AndroidViewModel(application)  {

    private var repository: UsersRepository = UsersRepository()

   /* private var SettingsDataStore: SettingsDataStore = SettingsDataStore(getApplication<Application>().applicationContext)*/

    private val _usersList = MutableLiveData<ArrayList<User>>()
    val usersList: LiveData<ArrayList<User>>
        get() = _usersList

  /*  init {
        getWallpaper()
    }*/

/*    private val _wallpaper = MutableLiveData<String>()
    val wallpaper : LiveData<String>
        get() = _wallpaper*/

/*    private fun getWallpaper(){
        viewModelScope.launch {
            SettingsDataStore.preferenceFlow.collectLatest {
                _wallpaper.value = it
            }
        }
    }*/

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