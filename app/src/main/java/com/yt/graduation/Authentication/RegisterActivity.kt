package com.yt.graduation.Authentication

import android.app.ProgressDialog
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yt.graduation.MainActivity
import com.yt.graduation.databinding.ActivityRegisterBinding

private lateinit var binding: ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var email: String
    private lateinit var name: String
    private lateinit var password2: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null) goToMainActivity()

        binding.registerButton.setOnClickListener() {
            register(view)
        }
        binding.goToLogin.setOnClickListener() {
            goToLogin(view)
        }


    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun register(view: View) {

        binding.apply {
            email = registerMail.text.toString()
            password = registerPassword.text.toString()
            name = registerName.text.toString()
            password2 = registerPasswordAgain.text.toString()
        }

        if (validateRegisteration(email,password,password2)){
            binding.registerProgressBar.visibility= View.VISIBLE
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name, password,email) //Go TO MainActivity if data added successfully
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
            binding.registerProgressBar.visibility= View.GONE
        }else{
            Toast.makeText(applicationContext, "Please check your information !", Toast.LENGTH_LONG).show()
        }

    }

    private fun addUserToDatabase(name: String, password: String, email: String) {
        val userId = auth.currentUser!!.uid //Get user unique id
        database = Firebase.database
        val dbRef = database.reference

        val user = hashMapOf<String,String>()
        user["name"] = name
        user["image"] = "default"

        dbRef.child("Users").child(userId) .setValue(user).addOnCompleteListener{  task ->
            if(task.isSuccessful)  goToMain()
        }
    }

    fun goToLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun validateRegisteration(email:String, passw1:String,passw2:String):Boolean{
        return email.takeLast(11)=="@itu.edu.tr" && passw1.isNotEmpty() && passw1.equals(passw2)
    }
}


