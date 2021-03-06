package com.yt.graduation.repository

import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.yt.graduation.UI.Settings.SettingsFragment
import com.yt.graduation.model.User
import com.yt.graduation.util.FirebaseResultListener
import com.yt.graduation.util.OnDataReceiveCallback
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.coroutineContext


class SettingsRepository {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database = Firebase.database
    private var storageRef = FirebaseStorage.getInstance().reference
    private lateinit var dbRefUser: DatabaseReference
    private var user = User()

    fun getUser( callback: OnDataReceiveCallback): User {
        if (auth.currentUser != null) {
            val userId = auth.currentUser!!.uid

            //Read From Database
            dbRefUser = database.reference.child("Users").child(userId)
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    user.name = dataSnapshot.child("name").value.toString() //User -> userId -> name
                    user.image = dataSnapshot.child("image").value.toString() //User -> userId -> image
                    //user.email = auth.currentUser!!.email.toString()
                    //user.registrationDate = dataSnapshot.child("registrationDate").value.toString()
                    callback.onDataReceived(user.name, user.image)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            dbRefUser.addValueEventListener(postListener)

            return user
        }
        return User()
    }

    fun setUser(user: User,resultListener: FirebaseResultListener) {
        if (auth.currentUser != null) {

            // Add User Image To Storage
            val userId = auth.currentUser!!.uid //get user unique Id
            val userImageRef = storageRef.child("profile_images").child("$userId.jpg")

            //Providing image uri  and //Add Image To Storage
            userImageRef.putFile(user.image.toUri()).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                userImageRef.downloadUrl
            }.addOnCompleteListener { task ->
                //After put file to storage, if the process is successful then add this to realtime DB
                if (task.isSuccessful) {
                    val downloadUri = task.result // Create HTTP link
                    val userUpdateMap = HashMap<String, Any>()
                    userUpdateMap["name"] = user.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()) else it.toString() }
                    userUpdateMap["image"] = downloadUri.toString()


                    //Also Update User Table (name and image uri)
                    dbRefUser = database.reference.child("Users").child(userId)
                    dbRefUser.updateChildren(userUpdateMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            resultListener.onSuccess(true)
                        }
                    }
                }
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }




}