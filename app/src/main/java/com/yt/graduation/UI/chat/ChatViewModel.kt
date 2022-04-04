package com.yt.graduation.UI.chat


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yt.graduation.model.Message
import com.yt.graduation.model.User
import com.yt.graduation.repository.ChatRepository
import com.yt.graduation.util.OnDataReceivedCallback

class ChatViewModel: ViewModel() {

    private var _messages = MutableLiveData<ArrayList<Message>>()
    val messages : LiveData<ArrayList<Message>>
        get() = _messages


    private var _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    private var _chatUID = MutableLiveData<String>()
    val chatUID : LiveData<String>
        get() = _chatUID

//    fun  setChatUID(chatUID: String){
//        _chatUID.postValue(chatUID)
//    }

    private val repository = ChatRepository()



    fun refreshMessages(productOwnerId:String){

        repository.getMessages(productOwnerId, object : OnDataReceivedCallback<Message>{
            override fun onDataReceived(list: ArrayList<Message>) {  //get messages
                _messages.postValue( list)
            }
            override fun onDataReceived(data: String) {
                TODO("Not yet implemented")
            }
        },
            object : OnDataReceivedCallback<String>{            //get chatUID
            override fun onDataReceived(list: ArrayList<String>) {
                TODO("Not yet implemented")
            }
            override fun onDataReceived(data: String) {
                _chatUID.postValue(data)
            }

        })


    }

    fun sendMessage(message: Message) {
       message.messageText = message.messageText.trim()
        if (message.messageText.isNotEmpty() || !message.messageImageUrl.isNullOrEmpty()){

            repository.sendMessage(chatUID.value.toString(),message,object :OnDataReceivedCallback<Message>{
                override fun onDataReceived(list: ArrayList<Message>) {
                    _messages.postValue( list)
                }

                override fun onDataReceived(data: String) {
                    TODO("Not yet implemented")
                }

            }, object : OnDataReceivedCallback<String>{
                override fun onDataReceived(list: ArrayList<String>) {

                }

                override fun onDataReceived(data: String) {
                    _chatUID.postValue(data)
                }

            })
        }

    }


}