package com.yt.graduation.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.yt.graduation.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext


class UsersRepository {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseRef = Firebase.database.reference
    private var storageRef = FirebaseStorage.getInstance().reference
    private var usersWithContact = ArrayList<User>()
    private val userId = auth.currentUser!!.uid

     fun getSpeeches(callback: OnDataReceiveCallback<User>){
        val usersWithContact = ArrayList<User>()
        val dbRefUsers = databaseRef.child("Users").orderByChild("name")
        dbRefUsers.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children){
                    // val user2 = ds.getValue(User::class.java) // get all User data
                    val user = User(
                        name = ds.child("name").value.toString(),
                        image = ds.child("image").value.toString(),
                        favoriteProducts = null,
                        uid = ds.child("uid").value.toString()
                    )
                    if (user.uid != auth.currentUser!!.uid){
                        usersWithContact.add(user)
                        Log.d("UsersFragment","-"+user.name+"-")
                    }
                }
                callback.onDataReceived(usersWithContact)

            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

     private fun  getChatIDs(callback: OnDataReceiveCallback<String>){

        val chatIDs = ArrayList<String>() //Contains all chatsUID

        val dbRefUserChats = databaseRef.child("UserChats").child(userId)
        dbRefUserChats.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children){
                    chatIDs.add(ds.value.toString())
                    Log.d("speeches", ds.key.toString()+"  "+ ds.value.toString())
                }
                callback.onDataReceived(chatIDs)
                Log.d("speeches chatIDs",chatIDs.toString() )
            }
            override fun onCancelled(databaseError: DatabaseError) { }
        }) //TODO add coroutine after this finish -go to ChatRooms


    }

     private fun getUserIds(callback: OnDataReceiveCallback<String>){
        getChatIDs(object :OnDataReceiveCallback<String>{
            override fun onDataReceived(chatIDs: ArrayList<String>) {
                //after find all chat rooms which user is in it
                //find that chat rooms in ChatRooms
                val userIds = ArrayList<String>()
                val dbRefChatRooms = databaseRef.child("ChatRooms")
                dbRefChatRooms.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children){ //ds.key -> chatUI'ler
                            if (chatIDs.contains(ds.key.toString())){
                                Log.d("Chat Rooms", (ds.child("members").value as List<String>).toString())
                                for (i in ds.child("members").value as List<String> ){ //there is to id in here my id and the other's id
                                    Log.d("Chat Rooms For i", i)
                                    if (i != userId){ //get only the other's id
                                        userIds.add(i)
                                        Log.d("Chat Rooms if i", i)
                                    }
                                }
                                callback.onDataReceived(userIds)
                                Log.d("Chat all user id with contact me -- my user id", "$userIds $userId")



                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }

        })

    }

    fun getUsersWithContact(callback: OnDataReceiveCallback<User>){
        getUserIds(object :OnDataReceiveCallback<String>{
            override fun onDataReceived(userIds: ArrayList<String>) {
                Log.d("Chat Rooms getUsersWithContact", "getUsersWithContact")
                val usersWithContact = ArrayList<User>()
                val dbRefUsers = databaseRef.child("Users").orderByChild("name")
                dbRefUsers.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children){
                            Log.d("Chat Rooms getUsersWithContact", ds.key.toString())
                            if (userIds.contains(ds.key.toString())){
                                // val user2 = ds.getValue(User::class.java) // get all User data
                                val user = User(
                                    name = ds.child("name").value.toString(),
                                    image = ds.child("image").value.toString(),
                                    favoriteProducts = null,
                                    uid = ds.child("uid").value.toString()
                                )
                                usersWithContact.add(user)

                            }
                            Log.d("Chat Rooms getUsersWithContact", usersWithContact.toString())
                        }
                        callback.onDataReceived(usersWithContact)


                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }

        })

    }

    interface OnDataReceiveCallback<T> {
        fun onDataReceived(users: ArrayList<T>)
    }


}