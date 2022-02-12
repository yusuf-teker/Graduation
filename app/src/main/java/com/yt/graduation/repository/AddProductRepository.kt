package com.yt.graduation.repository

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.yt.graduation.model.Product


class AddProductRepository {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database = Firebase.database
    private var storageRef = FirebaseStorage.getInstance().reference
    private var imageUri: Uri = Uri.EMPTY

    //Create a section "Products"
    var dbRefProducts = database.reference.child("Products")


    //Create a unique Key for each product !


    fun addProduct(product: Product): Boolean {
        var isAdded = false
        val productKey = dbRefProducts.push().key //push create a unique key

        if (productKey != null) {

            //Create a unique key and add product Image To Storage
            dbRefProducts = dbRefProducts.child(productKey)
            val productImageRef = storageRef.child("product_images").child("$productKey.jpg")
            val uploadTask = productImageRef.putFile(product.productImage.toUri())


            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                productImageRef.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    product.productImage = downloadUri.toString()
                    dbRefProducts.setValue(product).addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            isAdded=true
                        }

                    }
                } else {
                    // Handle failures
                    // ...
                }
            }

        }


        return isAdded
    }

    fun getCategories(): ArrayList<String> { //Return categories
        //Create a section "Categories"
        val dbRefCategories = database.reference.child("Categories")
        val categories = arrayListOf<String>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for (i in 0 until dataSnapshot.childrenCount.toInt()) {
                    categories.add(dataSnapshot.child("$i").value.toString())
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        dbRefCategories.addValueEventListener(postListener)
        return categories
    }
}



