package com.yt.graduation.UI.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.yt.graduation.UI.Account.AddProductViewModel
import com.yt.graduation.UI.Homepage.MainActivity
import com.yt.graduation.databinding.ActivityLoginBinding
import com.yt.graduation.model.User
import com.yt.graduation.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private lateinit var binding: ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginButton.setOnClickListener() {
            login()
        }
        binding.goToRegister.setOnClickListener() {
            goToRegister()
        }

        viewModel.userSignUpStatus.observe(this){
            when(it){
                is Resource.Success -> {
                    binding.loginProgressBar.visibility= View.GONE
                    goToMain()
                }
                is Resource.Loading -> {
                    binding.loginProgressBar.visibility= View.VISIBLE
                }
                is Resource.Error -> {
                    binding.loginProgressBar.visibility= View.GONE
                    Toast.makeText(applicationContext, "Giriş Yapılamadı", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun login() {
        binding.apply {
            viewModel.login(loginMail.text.toString(),loginPassword.text.toString())
        }
    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    private fun goToMain() {
        binding.loginProgressBar.visibility= View.GONE
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}