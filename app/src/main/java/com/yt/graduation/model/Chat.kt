package com.yt.graduation.model

data class Chat (
    var chatId: String? = null,
    var senderId: String? = null,
    var receiverId: String? = null,
    var isGroupChat: Boolean? = null
)