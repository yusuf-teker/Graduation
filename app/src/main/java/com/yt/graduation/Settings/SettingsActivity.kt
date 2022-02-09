package com.yt.graduation.Settings


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.yt.graduation.Authentication.LoginActivity
import com.yt.graduation.databinding.ActivitySettingsBinding
import java.util.*
import kotlin.collections.HashMap


private lateinit var binding: ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var imageUri: Uri? = null // The assignment is to be held on ResultActivity
    private lateinit var storageRef : StorageReference
    private lateinit var dbRefUser: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar( binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference


        if (auth.currentUser != null) {
            auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser!!.uid
            database = Firebase.database

            //Read From Database
            dbRefUser = database.reference.child("Users").child(userId)

            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user_name =
                        dataSnapshot.child("name").value.toString() //User -> userId -> name
                    val user_image = dataSnapshot.child("image").value.toString() //User -> userId -> image

                    binding.settingsUserName.setText(user_name)
                    /*
                    if(user_image != "default") {
                    }*/
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            dbRefUser.addValueEventListener(postListener)
        }else goToLogin()

        binding.signOutButton.setOnClickListener(){
            auth.signOut()
            goToLogin()
        }

        binding.userImage.setOnClickListener() {
            /*
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {// Permission is not granted
                //Request Permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
             */
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1
                )
            }
            // start picker to get image for cropping and then use the image in cropping activity
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)

            /*start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                .start(this)

             */

        }

        /**Update Database*/
        binding.updateButton.setOnClickListener(){
            val username= binding.settingsUserName.text.toString()
            if (username.isNotEmpty() && imageUri != null){ //if there is respone from crop activity imageUri is not null
                val userId = auth.currentUser!!.uid //get user unique Id
                //Get Reference (Where data is located in tree)
                val userImageRef = storageRef.child("profile_images").child(userId+".jpg")

                //Providing image uri to add to the database
                val uploadTask = userImageRef.putFile(imageUri!!)

                val urlTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    userImageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val userUpdateMap = HashMap<String,Any>()
                        userUpdateMap["name"] = username
                        userUpdateMap["image"] = downloadUri.toString()

                        //Also Update User Table (name and image uri)
                        dbRefUser.updateChildren(userUpdateMap).addOnCompleteListener{ task ->
                            if (task.isSuccessful){
                                Toast.makeText(applicationContext, "Update is successful", Toast.LENGTH_LONG).show()

                            }else Toast.makeText(applicationContext, "Update is not successful", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        // Handle failures
                        // ...
                    }
                }

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // Response from another activity
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) { // If response coming from crop activity
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) { //Image Cropped And Returned Successfully
                imageUri = result.uri
                binding.userImage.setImageURI(imageUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
    fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
    }

}