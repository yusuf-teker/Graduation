package com.yt.graduation.UI.Account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.yt.graduation.R
import com.yt.graduation.model.Product
import com.yt.graduation.repository.AddProductRepository
import java.util.*
import kotlin.collections.ArrayList

class AddProductViewModel : ViewModel()
{
    private val repository = AddProductRepository()
    var auth: FirebaseAuth = repository.auth
    val categories =  repository.getCategories()
    val categories2 = listOf<String>("Other","Text Book","Other Book","Note","Electronic","Furniture","Dress")


    private fun validateProduct(product:Product): Boolean{
        return (product.productName.length in 2..20 && product.productPrice in 0..1000000 && product.productDescription.length < 140)
    }

    fun addProduct(product: Product): Boolean{
        return if (validateProduct(product)){
            repository.addProduct(product)
        }else false
    }

}