package com.yt.graduation.UI.Account


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.yt.graduation.UI.Authentication.LoginActivity
import com.yt.graduation.R
import com.yt.graduation.UI.Homepage.MainActivity
import com.yt.graduation.databinding.ActivityAddProductBinding
import com.yt.graduation.model.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.URI
import java.time.LocalDateTime

private lateinit var binding: ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {

    private val viewModel: AddProductViewModel by viewModels()
    private val product = Product()
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (viewModel.auth.currentUser == null) goToLogin() //If there is no login, Go to login page

       // val categories = viewModel.categories //From Database //TODO
        val categories2 = viewModel.categories2

        val userId = viewModel.auth.currentUser!!.uid //Get user unique id

        //Spinner
        spinner = binding.productCategorySpinner
        val adapter = ArrayAdapter( this, R.layout.list_item_spinner,categories2)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : //Get selected category from spinner
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                product.productCategory = categories2[position]

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                product.productCategory = categories2[categories2.lastIndex]
            }
        }

        binding.productImageAdd.setOnClickListener(){
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
        } //Get image from Galery - Create a link - Assign link to product.productImage

        binding.addProductButton.setOnClickListener{

            // Fill Product Object
            binding.apply {
                product.productName = productNameEditText.text.toString()
                product.productPrice = productPriceEditText.text.toString().toInt()
                product.productDescription = productDescriptionEditText.text.toString()
                //product.productImage -> assign in onActivityResult
                product.productUploadDate = LocalDateTime.now().toString()
                product.productOwner = userId
            }

            //Add Product To Database
            if (viewModel.addProduct(product)){
                Toast.makeText(this@AddProductActivity, "Product added",Toast.LENGTH_LONG).show()
            } else Toast.makeText(this@AddProductActivity, "Product can not added",Toast.LENGTH_LONG).show()

        } //endOf onClickListener



    }

    private fun goToAddProduct() {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
    }
    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // Response from another activity
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) { // If response coming from crop activity
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) { //Image Cropped And Returned Successfully
                product.productImage = result.uri.toString()
                binding.productImageAdd.setImageURI(product.productImage.toUri())
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

}