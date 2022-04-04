package com.yt.graduation.repository


import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.yt.graduation.model.Message
import com.yt.graduation.util.OnDataReceivedCallback

class ChatRepository {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseRef = Firebase.database.reference
    private var storageRef = FirebaseStorage.getInstance().reference
    private val userId = auth.currentUser!!.uid
   // private var message = Message()


    fun sendMessage(chatUID: String?,message: Message,callback: OnDataReceivedCallback<Message>, callbackForChatUID: OnDataReceivedCallback<String>){
        //Todo
        //if there is no chat creat a chat id, else get chat id when user selected
        Log.d("chatUID",chatUID.toString())

        if (!chatUID.isNullOrEmpty()){

            message.senderId = auth.currentUser!!.uid
            val dbRefMessages =  databaseRef.child("Messages").child(chatUID)
            dbRefMessages.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var messageList= ArrayList<Message>()
                    for (ds in snapshot.children){ //get all old messages
                        var message= Message()
                        message.messageText = ds.child("messageText").value.toString()
                        message.id = ds.child("id").value.toString()
                        message.messageImageUrl = ds.child("messageImageUrl").value.toString()
                        message.receiverId = ds.child("receiverId").value.toString()
                        message.senderId = ds.child("senderId").value.toString()
                        message.timeToSend = ds.child("timeToSend").value.toString()
                        messageList.add(message)
                    }
                    messageList.add(message) //add new messages
                    val messageKey = dbRefMessages.push().key
                    dbRefMessages.child(messageKey!!).setValue(message)

                    callback.onDataReceived(messageList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }else{ //first time message
            val chatUID = databaseRef.child("Messages").push().key
            callbackForChatUID.onDataReceived(chatUID.toString())
            message.senderId = auth.currentUser!!.uid
            val messageKey = databaseRef.child("Messages").child(chatUID!!).push().key
            message.id = messageKey.toString()
            databaseRef.child("Messages").child(chatUID).child(messageKey!!).setValue(message)
            databaseRef.child("ChatRooms").child(chatUID).child("members").setValue(listOf(auth.currentUser!!.uid,message.receiverId))
            databaseRef.child("UserChats").child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var usersChatIds = ArrayList<String>()
                    if (snapshot.value!=null){
                        usersChatIds = snapshot.value as ArrayList<String>
                    }
                    usersChatIds.add(chatUID)

                    databaseRef.child("UserChats").child(auth.currentUser!!.uid).setValue(usersChatIds)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            databaseRef.child("UserChats").child(message.receiverId).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var usersChatIds = ArrayList<String>()
                    if (snapshot.value!=null){
                        usersChatIds = snapshot.value as ArrayList<String>
                    }
                    usersChatIds.add(chatUID)
                    databaseRef.child("UserChats").child(message.receiverId).setValue(usersChatIds)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



        }
    }

    fun getMessages(productOwnerId: String?,callback: OnDataReceivedCallback<Message>, callbackForChatUID: OnDataReceivedCallback<String>) {
        checkIsThereContactWithProductOwner(productOwnerId.toString(),object : OnDataReceivedCallback<String>{
            override fun onDataReceived(list: ArrayList<String>) {
                TODO("Not yet implemented")
            }

            override fun onDataReceived(chatUID: String) {//chatUID which owner and current user's unique Chat Id
                var messageList= ArrayList<Message>()
                if (chatUID.isNotEmpty()){
                    callbackForChatUID.onDataReceived(chatUID)
                    val dbRefMessages =  databaseRef.child("Messages").child(chatUID)
                    dbRefMessages.addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {

                            for (ds in snapshot.children){
                                var message= Message()
                                message.messageText = ds.child("messageText").value.toString()
                                message.id = ds.child("id").value.toString()
                                message.messageImageUrl = ds.child("messageImageUrl").value.toString()
                                message.receiverId = ds.child("receiverId").value.toString()
                                message.senderId = ds.child("senderId").value.toString()
                                message.timeToSend = ds.child("timeToSend").value.toString()
                                messageList.add(message)
                            }
                            callback.onDataReceived(messageList)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
                else{
                    callback.onDataReceived(messageList)
                    callbackForChatUID.onDataReceived(chatUID)
                }
            }

        })




    }



    //get all chat ids from UserChats
    private fun  getChatIDs(callback: UsersRepository.OnDataReceiveCallback<String>){

        val chatIDs = ArrayList<String>() //Contains all chatsUID

        val dbRefUserChats = databaseRef.child("UserChats").child(userId)
        dbRefUserChats.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children){
                    chatIDs.add(ds.value.toString())
                }
                //if there are chatIDs check if any of them is belong to sender and receiver
                callback.onDataReceived(chatIDs)


                Log.d("speeches chatIDs",chatIDs.toString() )
            }
            override fun onCancelled(databaseError: DatabaseError) { }
        })


    }

    // get chatId between currentUser and productOwner
    private fun checkIsThereContactWithProductOwner(productOwnerId: String, callback: OnDataReceivedCallback<String>){
        getChatIDs(object : UsersRepository.OnDataReceiveCallback<String> {
            override fun onDataReceived(chatIDs: ArrayList<String>) {
                if (chatIDs.size!=0){ // If user has CHAT
                    databaseRef.child("ChatRooms").addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (ds in snapshot.children){ //// ChatRooms -> ChatUID'ler
                                if (chatIDs.contains(ds.key.toString())){ // ChatRooms içindeki ChatUID'ler ile CurrentUser'ın kesiştiği Chat UID'ler var ise
                                    for (i in ds.children){// ChatRooms -> ChatUID -> members
                                        val memberList = i.value as List<String>                                //Todo make it safe cast
                                        // we came from userId so only check if the product ownerId is in memberList or not
                                        if( memberList.contains(productOwnerId) && userId != productOwnerId) { //also check productOwner is not Current user
                                            callback.onDataReceived(ds.key.toString()) //if we find the chatUID with currentUser and productOwner
                                            return
                                        }
                                    }

                                }
                            }
                            callback.onDataReceived("")//// If user has no CHAT with ProductOwner
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }else{ // If user have zero CHAT
                    callback.onDataReceived("")
                }
            }
        })
    }



}