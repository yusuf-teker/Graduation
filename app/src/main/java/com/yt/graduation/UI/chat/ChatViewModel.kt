package com.yt.graduation.UI.chat


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yt.graduation.model.Message
import com.yt.graduation.repository.ChatRepository

class ChatViewModel: ViewModel() {

    private var _messages = MutableLiveData<ArrayList<Message>>()
    val messages : LiveData<ArrayList<Message>>
        get() = _messages

    private val repository = ChatRepository()



    fun refreshMessages(){
        _messages.postValue( arrayListOf<Message>( Message("Yusuf","Dilber","First Message","16:24 29.03.2022")))

        repository.getProducts()
    }






}