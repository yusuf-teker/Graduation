package com.yt.graduation.UI.Settings

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.yt.graduation.R
import com.yt.graduation.databinding.FragmentSettingsBinding
import com.yt.graduation.model.User
import com.yt.graduation.util.FirebaseResultListener
import com.yt.graduation.util.OnDataReceiveCallback


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var user : User
    private var imageUri: Uri = Uri.EMPTY // The assignment is to be held onResultActivity
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        //Get User From Database and update UI
        user = viewModel.getUser(object : OnDataReceiveCallback {
            override fun onDataReceived(display_name: String, photo: String) {
                binding.settingsUserName.setText(display_name)
                if (photo=="default" || photo.isEmpty()){ //if user didn't add a profile photo
                    binding.userImageSettings.setBackgroundResource(R.drawable.defaultuser)
                }else{
                    activity?.let {
                        Glide.with(it)
                            .load(photo) // image url
                            .into(binding.userImageSettings)
                    }
                }
            }
        })

        binding.signOutButton.setOnClickListener{ signOut() }

        binding.userImageSettings.setOnClickListener {
            if (activity?.let { it1 ->
                    ContextCompat.checkSelfPermission(
                        it1,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let { it1 ->
                    ActivityCompat.requestPermissions(
                        it1,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1
                    )
                }
            }

            // start picker to get image for cropping and then use the image in cropping activity
            context?.let { it1 ->
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(it1, this)
            }

        }

        /**Update Database*/
        binding.updateButton.setOnClickListener{
            updateDB()
        }

        return binding.root
    }

    private fun updateDB() {
        user.name = binding.settingsUserName.text.toString()
        user.image = imageUri.toString()
        viewModel.setUser(user, resultListener = object : FirebaseResultListener {
            override fun onSuccess(isSuccess: Boolean) {
                Toast.makeText(context, "Update process is finished successfully", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun signOut() {
        viewModel.signOut()
        goToLogin()
    }

    //Set Image Uri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // Response from another activity
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) { // If response coming from crop activity
            val result = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) { //Image Cropped And Returned Successfully
                imageUri = result.uri
                binding.userImageSettings.setImageURI(imageUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.error
            }
        }
    }

    private fun goToLogin() {
        findNavController().navigate(R.id.loginFragment)
    }
    
}