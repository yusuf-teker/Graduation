package com.yt.graduation.model

data class Message(
    var sender : String,
    var receiver: String,
    var messsageContent: String,
    val timeToSend: String
)