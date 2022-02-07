package com.yt.graduation.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.yt.graduation.MainActivity
import com.yt.graduation.databinding.ActivityLoginBinding
import com.yt.graduation.databinding.ActivityRegisterBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private lateinit var binding: ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener() {
            login(view)
        }
        binding.goToRegister.setOnClickListener() {
            goToRegister(view)
        }


    }

    fun login(view: View) {
        binding.apply {
            email = loginMail.text.toString()
            password = loginPassword.text.toString()

        }
        //this part will be changed
        binding.loginProgressBar.visibility= View.VISIBLE
        lifecycleScope.launch {
            delay(4000) // debounce effect
            binding.loginProgressBar.visibility= View.GONE
        }/////////////////////////////////////////////////

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }

    fun goToRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}