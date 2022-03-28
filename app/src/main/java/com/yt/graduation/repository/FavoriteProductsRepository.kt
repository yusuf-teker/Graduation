package com.yt.graduation.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yt.graduation.model.Product

class FavoriteProductsRepository {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userRef: DatabaseReference
    private lateinit var productRef: DatabaseReference
    private lateinit var database: FirebaseDatabase

    fun refreshData(callback: OnDataReceiveCallback){
        if (auth.currentUser!=null){
            database = Firebase.database
            val userId = auth.currentUser!!.uid
            userRef = database.reference.child("Users").child(userId)
            productRef = database.reference.child("Products")

            val favoriteProductsNames = ArrayList<String>()
            userRef.child("favoriteProducts").get().addOnSuccessListener {
                if (it.value!=null){
                    favoriteProductsNames.addAll( it.value as ArrayList<String>)
                    for (i in it.value as ArrayList<String>){
                        Log.d("Favorites  ",productRef.equalTo(i).toString())
                    }
                    val favoriteProductsList = ArrayList<Product>()
                    productRef.addListenerForSingleValueEvent(object :ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (ds in snapshot.children){
                                if (favoriteProductsNames.contains(ds.child("productKey").value.toString())){
                                    val product = Product(
                                        ds.child("productName").value.toString(),
                                        ds.child("productPrice").value.toString().toInt(),
                                        ds.child("productDescription").value.toString(),
                                        ds.child("productCategory").value.toString(),
                                        ds.child("productUploadDate").value.toString(),
                                        ds.child("productOwner").value.toString(),
                                        ds.child("productImage").value.toString(),
                                        ds.child("productState").value.toString().toBoolean(),
                                        ds.child("productKey").value.toString())
                                    favoriteProductsList.add(product)
                                }
                            }
                            callback.onDataReceived(favoriteProductsList)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                }

            }
        }
    }




    interface OnDataReceiveCallback {
        fun onDataReceived(productList: ArrayList<Product>)
    }
}