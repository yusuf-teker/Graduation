package com.yt.graduation.UI.Settings


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.yt.graduation.R
import com.yt.graduation.UI.Authentication.LoginActivity
import com.yt.graduation.UI.Homepage.MainActivity
import com.yt.graduation.databinding.ActivitySettingsBinding
import com.yt.graduation.model.User
import com.yt.graduation.repository.SettingsRepository


private lateinit var binding: ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {


    private var imageUri: Uri = Uri.EMPTY // The assignment is to be held onResultActivity

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var user : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar( binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolBar.setNavigationOnClickListener {
            //do something you want
            onBackPressed()
        }

        //Get User From Database and update UI
        user = viewModel.getUser(object :SettingsRepository.OnDataReceiveCallback{
            override fun onDataReceived(display_name: String, photo: String) {
                binding.settingsUserName.setText(display_name)
                if (photo=="default"){
                    binding.userImage.setBackgroundResource(R.drawable.defaultuser)
                }else{
                    Glide.with(this@SettingsActivity)
                        .load(photo) // image url
                        .into(binding.userImage)
                }


            }
        })



        binding.signOutButton.setOnClickListener{
            viewModel.signOut()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            goToLogin()
        }

        binding.userImage.setOnClickListener {
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
        binding.updateButton.setOnClickListener{
            user.name = binding.settingsUserName.text.toString()
            user.image = imageUri.toString()
            Toast.makeText(this,user.name.toString(),Toast.LENGTH_SHORT).show()
            viewModel.setUser(user)
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
                result.error
            }
        }
    }
    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


}