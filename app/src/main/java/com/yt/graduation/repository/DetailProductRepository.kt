package com.yt.graduation.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yt.graduation.model.User
import com.yt.graduation.util.FirebaseResultListener
import com.yt.graduation.util.OnDataReceivedCallback


class DetailProductRepository {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userRef: DatabaseReference

    private var user = User()
    private val userId = auth.currentUser!!.uid
    private var databaseRef = Firebase.database.reference


    fun addToFavorites(productKey: String, resultListener: FirebaseResultListener){
        if(auth.currentUser != null){

            val userId = auth.currentUser!!.uid
            userRef = databaseRef.child("Users").child(userId)

            userRef.child("favoriteProducts").get().addOnSuccessListener {
                val productList = ArrayList<String>()

                if (it.value!=null){
                    productList.addAll(it.value as ArrayList<String>)
                    if (!productList.contains(productKey)){
                        productList.add(productKey)
                        resultListener.onSuccess(true)
                    }
                    else{
                        productList.remove(productKey)
                        resultListener.onSuccess(false)
                    }

                    Log.i("firebase", "Favorites $productList")
                    userRef.child("favoriteProducts").setValue(productList)
                }else{
                    productList.add(productKey)
                    userRef.child("favoriteProducts").setValue(productList)
                }
            }

        }
    }

    fun isFavorite(productKey: String,resultListener: FirebaseResultListener){
        if(auth.currentUser != null){

            val userId = auth.currentUser!!.uid
            userRef = databaseRef.child("Users").child(userId)
            userRef.child("favoriteProducts").get().addOnSuccessListener {
                if (it.value!=null && (it.value as ArrayList<String>).contains(productKey))
                    resultListener.onSuccess(true)
                else resultListener.onSuccess(false)
            }
        }
    }

    fun getOwnerInfo(ownerId: String,callback: OnDataReceiveCallback) {

        userRef = databaseRef.child("Users").child(ownerId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").value.toString() //User -> userId -> name
                val imageUrl = snapshot.child("image").value.toString() //User -> userId -> image
                val id = snapshot.child("uid").value.toString()
                callback.onDataReceived(arrayListOf(name, imageUrl,id))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    //get all chat ids from UserChats
//    private fun  getChatIDs(callback: UsersRepository.OnDataReceiveCallback<String>){
//
//        val chatIDs = ArrayList<String>() //Contains all chatsUID
//
//        val dbRefUserChats = databaseRef.child("UserChats").child(userId)
//        dbRefUserChats.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (ds in dataSnapshot.children){
//                    chatIDs.add(ds.value.toString())
//                    Log.d("speeches", ds.key.toString()+"  "+ ds.value.toString())
//                }
//                //if there are chatIDs check if any of them is belong to sender and receiver
//                callback.onDataReceived(chatIDs)
//
//
//                Log.d("speeches chatIDs",chatIDs.toString() )
//            }
//            override fun onCancelled(databaseError: DatabaseError) { }
//        }) //TODO add coroutine after this finish -go to ChatRooms
//
//
//    }



//    fun checkIsThereContactWithProductOwner(productOwnerId: String,callback: OnDataReceivedCallback<String>){
//        getChatIDs(object : UsersRepository.OnDataReceiveCallback<String> {
//            override fun onDataReceived(chatIDs: ArrayList<String>) {
//                if (chatIDs.size!=0){
//                    databaseRef.child("ChatRooms").addListenerForSingleValueEvent(object :ValueEventListener{
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            for (ds in snapshot.children){ //ds.key -> chatUID'ler
//                                if (chatIDs.contains(ds.key.toString())){
//                                    for (i in ds.children){
//                                        val memberList = i.value as List<String> //Todo make it safe cast
//                                        if( memberList.contains(userId) && memberList.contains(productOwnerId) && userId != productOwnerId) { //there is to id in here my id and the other's id
//                                            callback.onDataReceived(ds.key.toString())
//                                            return
//                                        }
//                                    }
//                                    callback.onDataReceived("")
//                                }
//                            }
//                        }
//                        override fun onCancelled(error: DatabaseError) {
//                            TODO("Not yet implemented")
//                        }
//                    })
//                }else{
//                    callback.onDataReceived("")
//                }
//            }
//        })
//    }










    interface OnDataReceiveCallback {
        fun onDataReceived(ownerInfo: ArrayList<String>)
    }


}