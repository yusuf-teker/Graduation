package com.yt.graduation.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.yt.graduation.MainActivity
import com.yt.graduation.databinding.ActivityLoginBinding
import com.yt.graduation.databinding.ActivityRegisterBinding

private lateinit var binding: ActivityLoginBinding
private lateinit var email: String
private lateinit var password: String

class LoginActivity : AppCompatActivity() {

    private lateinit var  auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth= FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener(){
            login(view)
        }


    }
    fun login(view: View){
        binding.apply {
            email= loginMail.text.toString()
            password= loginPassword.text.toString()

        }

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    fun goToRegister(view: View){
        val intent= Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }
}