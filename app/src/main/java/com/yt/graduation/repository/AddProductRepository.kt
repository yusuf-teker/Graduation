package com.yt.graduation.repository


import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.yt.graduation.model.Product
import com.yt.graduation.util.FirebaseResultListener
import kotlin.collections.ArrayList


class AddProductRepository {
    private var isAdded = false
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database = Firebase.database
    private var storageRef = FirebaseStorage.getInstance().reference

     fun addProduct(product: Product,  onCompletedListener: FirebaseResultListener) {

        //Create a section "Products"
        var dbRefProducts = database.reference.child("Products")
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
                    product.productOwner = auth.currentUser!!.uid
                    product.productKey = productKey
                    isAdded=true

                   dbRefProducts.setValue(product).addOnCompleteListener{ task ->

                       onCompletedListener.onSuccess(task.isSuccessful)
                    }
                } else {
                    onCompletedListener.onSuccess(task.isCanceled)
                }
            }
        }
    }


     fun getCategories(callback: OnDataReceiveCallback): ArrayList<String> { //Return categories
        //Create a section "Categories"
        val dbRefCategories = database.reference.child("Categories")
        val categories = arrayListOf<String>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for (i in 0 until dataSnapshot.childrenCount.toInt()) {
                    categories.add(dataSnapshot.child("$i").value.toString())
                }
                callback.onDataReceived(categories)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        dbRefCategories.addValueEventListener(postListener)
        return categories
    }

    fun getProducts(): ArrayList<Product>{
        val allProducts = ArrayList<Product>()
        val dbRefCategories = database.reference.child("Products")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val product = Product()
                for (ds  in  dataSnapshot.children) {
                    for (pd in ds.children){
                        product.productName = pd.child("name").toString()
                    }
                }

                allProducts.add(product)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        dbRefCategories.addValueEventListener(postListener)
        return arrayListOf()
    }

    interface OnDataReceiveCallback {
        fun onDataReceived(categories: ArrayList<String>)
    }

}



