package com.yt.graduation.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yt.graduation.model.User
import com.yt.graduation.util.FirebaseResultListener


class DetailProductRepository {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userRef: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var user = User()



    fun addToFavorites(productKey: String, resultListener: FirebaseResultListener){
        if(auth.currentUser != null){
            database = Firebase.database
            val userId = auth.currentUser!!.uid
            userRef = database.reference.child("Users").child(userId)

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
            database = Firebase.database
            val userId = auth.currentUser!!.uid
            userRef = database.reference.child("Users").child(userId)
            userRef.child("favoriteProducts").get().addOnSuccessListener {
                if (it.value!=null && (it.value as ArrayList<String>).contains(productKey))
                    resultListener.onSuccess(true)
                else resultListener.onSuccess(false)
            }
        }
    }

}