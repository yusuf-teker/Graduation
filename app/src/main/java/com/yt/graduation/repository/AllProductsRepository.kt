package com.yt.graduation.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yt.graduation.model.Product

class AllProductsRepository {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database = Firebase.database

     fun getProducts( callback: OnDataReceiveCallback): ArrayList<Product>{
        val allProducts = ArrayList<Product>()
        val dbRefProducts = database.reference.child("Products")
        dbRefProducts.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children){
                    val product = Product(
                        ds.child("productName").value.toString(),
                        ds.child("productPrice").value.toString().toInt(),
                        ds.child("productDescription").value.toString(),
                        ds.child("productCategory").value.toString(),
                        ds.child("productUploadDate").value.toString(),
                        ds.child("productOwner").value.toString(),
                        ds.child("productImage").value.toString(),
                        ds.child("productState").value.toString().toBoolean(),
                    ds.child("productKey").value.toString()
                    )
                    allProducts.add(product)
                    Log.d("AllProductFragment","-"+allProducts[0].productName+"-")

                }
                callback.onDataReceived(allProducts)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        return allProducts
    }

    interface OnDataReceiveCallback {
        fun onDataReceived(productList: ArrayList<Product>)
    }
}