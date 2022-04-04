package com.yt.graduation.model

data class Message(
    var id: String,
    var senderId : String,
    var receiverId: String,
    var messageText: String,
    var messageImageUrl: String? = null,
    var timeToSend: String
){
    constructor() : this("", "", "", "", "","")
}