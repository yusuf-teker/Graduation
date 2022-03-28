package com.yt.graduation.UI.Account

import android.content.Intent
import android.content.pm.PackageManager
import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.yt.graduation.R
import com.yt.graduation.UI.Homepage.MainActivity
import com.yt.graduation.databinding.AddProductFragmentBinding
import com.yt.graduation.model.Product
import com.yt.graduation.util.FirebaseResultListener
import java.time.LocalDateTime

class AddProductFragment : Fragment() {
    private val product = Product()


    private lateinit var viewModel: AddProductViewModel
    private var _binding: AddProductFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = "Add Product"

        viewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        viewModel.addProductFragment = this
        if (viewModel.auth.currentUser == null) goToLogin() //If there is no login, Go to login page
        viewModel.getCategories()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddProductFragmentBinding.inflate(inflater, container, false)

        viewModel.categories.observe(viewLifecycleOwner){
            binding.productCategorySpinner.adapter = ArrayAdapter( requireContext(), R.layout.list_item_spinner,it)
            binding.productCategorySpinner.onItemSelectedListener = object : //Get selected category from spinner
                AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    product.productCategory = it[position]

                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    product.productCategory = it[it.lastIndex]
                }
            }
        }

        viewModel.isAdded.observe(viewLifecycleOwner){
            if (it){
                binding.addProductProgressBar.visibility = View.GONE
            }else binding.addProductProgressBar.visibility = View.VISIBLE
        }


        binding.productImageAdd.setOnClickListener(){

            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1
                )
            }
            // start picker to get image for cropping and then use the image in cropping activity
            context?.let { it1 ->
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(it1, this)
            }
        } //Get image from Galery - Create a link - Assign link to product.productImage

        binding.addProductButton.setOnClickListener{

            // Fill Product Object
            binding.apply {
                product.productName = productNameEditText.text.toString()
                product.productPrice =  if(productPriceEditText.text.toString().isNotEmpty()) productPriceEditText.text.toString().toInt() else 0
                product.productDescription = productDescriptionEditText.text.toString()
                //product.productImage -> assign in onActivityResult
                product.productUploadDate = LocalDateTime.now().toString()
            }

            //Add Product To Database

           viewModel.addProduct(product, object : FirebaseResultListener{
               override fun onSuccess(isSuccess: Boolean) {
                   if (isSuccess){
                       goToMain()
                   }
                   else{
                       Toast.makeText( requireActivity(), "Product can not added", Toast.LENGTH_LONG).show()
                   }
               }
           })

        }
        return binding.root
    }

    private fun goToMain() {
        viewModel.setIsAdded(true)
        findNavController().navigate(R.id.allProductsFragment)
    }
    private fun goToLogin() {
        findNavController().navigate(R.id.loginFragment)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // Response from another activity
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) { // If response coming from crop activity
            val result = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) { //Image Cropped And Returned Successfully
                product.productImage = result.uri.toString()
                binding.productImageAdd.setImageURI(product.productImage.toUri())
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}