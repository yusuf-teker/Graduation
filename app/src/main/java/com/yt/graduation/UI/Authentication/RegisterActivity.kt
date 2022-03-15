package com.yt.graduation.UI.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yt.graduation.UI.Homepage.MainActivity
import com.yt.graduation.databinding.ActivityRegisterBinding
import com.yt.graduation.model.User
import com.yt.graduation.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


private lateinit var binding: ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null) goToMain()

        binding.registerButton.setOnClickListener() {
            register(view)
        }
        binding.goToLogin.setOnClickListener() {
            goToLogin(view)
        }

        viewModel.userRegistrationStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.registerProgressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.registerProgressBar.isVisible = false
                    Toast.makeText(applicationContext, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    goToMain()
                }
                is Resource.Error -> {
                    binding.registerProgressBar.isVisible = false
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


    }


    fun register(view: View) {
        val user = User()
        binding.apply {
            user.email = registerMail.text.toString()
            user.name = registerName.text.toString()
            //registerProgressBar.visibility= View.VISIBLE
        }
        viewModel.register(user,binding.registerPassword.text.toString(),binding.registerPasswordAgain.text.toString())
//        if (viewModel.validateRegisteration(user.name,user.email,binding.registerPassword.text.toString(),binding.registerPasswordAgain.text.toString())){
//
//            auth.createUserWithEmailAndPassword(user.email, binding.registerPassword.text.toString()).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    addUserToDatabase(user) //Go TO MainActivity if data added successfully
//                }
//            }.addOnFailureListener { exception ->
//                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
//            }
//
//        }else{
//            Toast.makeText(applicationContext, "Please check your information !", Toast.LENGTH_LONG).show()
//        }
//        binding.registerProgressBar.visibility = GONE
//
    }

//    private  fun addUserToDatabase(user: User) {
//
//        val userId = auth.currentUser!!.uid //Get user unique id
//        database = Firebase.database
//        val dbRef = database.reference
//        dbRef.child("Users").child(userId) .setValue(user).addOnCompleteListener{  task ->
//            if(task.isSuccessful)  goToMain()
//        }
//
//    }

    fun goToLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }



    override fun onStart() {
        super.onStart()
        if(auth.currentUser!=null) goToMain()
    }
}


