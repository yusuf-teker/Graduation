package com.yt.graduation.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.yt.graduation.model.Product

class OnSaleProductsRepository {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var productsRef: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef: StorageReference

    fun getProducts( callback: OnDataReceiveCallback): ArrayList<Product>{
        val onSaleProducts = ArrayList<Product>()
        database = Firebase.database
        val dbRefProducts = database.reference.child("Products")
        dbRefProducts.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children){
                    if ( ds.child("productOwner").value.toString() == auth.uid){ // if productId matches with user id then add get the product
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
                        onSaleProducts.add(product)
                    }

                }
                callback.onDataReceived(onSaleProducts)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        return onSaleProducts
    }

    fun deleteProduct(productKey: String){
        if (auth.currentUser!=null){
            database = Firebase.database
            productsRef = database.reference.child("Products")

            val currentProduct = productsRef.child(productKey)
            currentProduct.child("productImage").get().addOnSuccessListener {
                Log.d("Favorite",it.value.toString())
                val url = it.value.toString()
                storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)
                currentProduct.removeValue()
                storageRef.delete().addOnSuccessListener {
                    Log.e("Picture","#deleted");
                }
            }

        }

    }

    interface OnDataReceiveCallback {
        fun onDataReceived(productList: ArrayList<Product>)
    }
}