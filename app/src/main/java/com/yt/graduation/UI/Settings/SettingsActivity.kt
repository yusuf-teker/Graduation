package com.yt.graduation.UI.Settings


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
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
import com.yt.graduation.UI.Authentication.LoginActivity
import com.yt.graduation.databinding.ActivitySettingsBinding
import com.yt.graduation.model.User
import com.yt.graduation.repository.SettingsRepository


private lateinit var binding: ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var imageUri: Uri = Uri.EMPTY // The assignment is to be held onResultActivity
    private lateinit var storageRef : StorageReference
    private lateinit var dbRefUser: DatabaseReference
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var user : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar( binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
        database = Firebase.database

        //Get User From Database and update UI
        user = viewModel.getUser(object :SettingsRepository.OnDataReceiveCallback{
            override fun onDataReceived(display_name: String, photo: String) {
                binding.settingsUserName.setText(display_name)
                Glide.with(this@SettingsActivity)
                    .load(photo) // image url
                    .into(binding.userImage);

            }
        })



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


        }

        /**Update Database*/
        binding.updateButton.setOnClickListener(){

           // user = viewModel.getUser()
            val username= binding.settingsUserName.text.toString()
            user.name = username
            user.image = imageUri.toString()

            //viewModel.setUser(user)

            if (username.isNotEmpty()){ //if there is response from crop activity imageUri is not null

                // Add User Image To Storage
                val userId = auth.currentUser!!.uid //get user unique Id
                val userImageRef = storageRef.child("profile_images").child(userId+".jpg")
                    //Providing image uri to add to the database //Add Image To Storage
                Log.d("imageUri", user.image)
                Log.d("imageUri","$imageUri")
                val uploadTask = userImageRef.putFile(imageUri).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    userImageRef.downloadUrl
                }.addOnCompleteListener { task ->

                    //After put file to storage, if the process is successfull then add this to realtime DB
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        Log.d("SettingsActivity downloadUri",downloadUri.toString())
                        val userUpdateMap = HashMap<String,Any>()
                        userUpdateMap["name"] = username
                        userUpdateMap["image"] = downloadUri.toString()
                        dbRefUser = database.reference.child("Users").child(userId)
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

    //Set Image Uri
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