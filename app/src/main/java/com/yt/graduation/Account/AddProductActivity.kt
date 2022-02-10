package com.yt.graduation.Account


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.yt.graduation.Authentication.LoginActivity
import com.yt.graduation.MainActivity
import com.yt.graduation.R
import com.yt.graduation.databinding.ActivityAddProductBinding
import java.time.LocalDateTime
import kotlin.properties.Delegates

private lateinit var binding: ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef : StorageReference
    private lateinit var dbRef: DatabaseReference
    private lateinit var spinner: Spinner

    private lateinit var productName: String
    private var productPrice by Delegates.notNull<Int>()
    private lateinit var productDescription: String

    private lateinit var viewModel: AddProductViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
        spinner= binding.productCategorySpinner
        val categories = resources.getStringArray(R.array.Languages) //For spinner // TODO get it from db
        if (auth.currentUser == null) goToLogin() //If there is no login, Go to login page
        else{                                     //Else access the Database
            database = Firebase.database
            dbRef = database.reference
        }

        //Spinner
        val adapter = ArrayAdapter( //Create Spinner Adapter
            this@AddProductActivity,
            R.layout.list_item_spinner, categories)
        spinner.adapter = adapter   //Attach it to spinner


        binding.addProductButton.setOnClickListener{

            val userId = auth.currentUser!!.uid //Get user unique id


            val product = hashMapOf<String,String>()
            binding.apply {
                productName = productNameEditText.text.toString()
                productPrice = productPriceEditText.text.toString().toInt()
                productDescription = productDescriptionEditText.text.toString()

                spinner.onItemSelectedListener = object : //Get selected category from spinner
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>,
                                                view: View, position: Int, id: Long) {
                        product["product_category"] = categories[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        product["product_category"] = categories.last() //Default Category -> Other
                    }
                }
                if(validateProduct(productName,productPrice)){
                    product["product_name"] = productName
                    product["product_description"] = productDescription
                    product["product_upload_date"] = LocalDateTime.now().toString()
                    product["product_state"] = "on_sale"
                    product["product_image"] = "default"
                    product["product_price"] = productPrice.toString()
                    product["product_owner"] = userId

                    val dbRefProducts = dbRef.child("Products") //Create a section "Products"
                    //Create a unique Key for each product !
                    val key = dbRefProducts.push().key
                    if (key != null) {

                        dbRefProducts.child(key).setValue(product).addOnCompleteListener{  task ->
                            if(task.isSuccessful){
                                Toast.makeText(this@AddProductActivity, "Product Added Successfuly",Toast.LENGTH_LONG )
                                goToMain()
                            }
                        }
                    }
                }

            }


        } //endOf onClickListener



    }

    fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    fun validateProduct(productName:String, productPrice: Int): Boolean{
        return productName.isNotEmpty() && (productPrice in 0..999999)
    }
}