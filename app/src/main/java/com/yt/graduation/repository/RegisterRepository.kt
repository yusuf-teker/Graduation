package com.yt.graduation.repository

import android.content.Intent
import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yt.graduation.UI.Homepage.MainActivity
import com.yt.graduation.model.User
import com.yt.graduation.util.Resource
import com.yt.graduation.util.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RegisterRepository {

    private var auth: FirebaseAuth= FirebaseAuth.getInstance()
    private var databaseRef = Firebase.database.reference

    suspend fun register(user: User,password:String) : Resource<AuthResult>{
        return withContext(Dispatchers.IO){
            safeCall { //herhangi bir yerde exception çıkarsa Error classını döndürür
                val registrationResult =  auth.createUserWithEmailAndPassword(user.email, password).await()
                val userId = auth.currentUser!!.uid //Get user unique id
                databaseRef.child("Users").child(userId).setValue(user).await()//Add Database->Users->uniqueUserId->all variable in user Model
                Resource.Success(registrationResult) //Hata olmazsa Success döndür
                }
            }
        }
    }
