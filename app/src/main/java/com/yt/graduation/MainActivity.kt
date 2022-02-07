package com.yt.graduation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yt.graduation.Authentication.LoginActivity
import com.yt.graduation.databinding.ActivityMainBinding


private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar( binding.mainToolBar)



        auth = FirebaseAuth.getInstance()
        if ( auth.currentUser != null ){
            val database = Firebase.database
            val myRef = database.getReference("message")

            myRef.setValue("Hello, Worl2gd!")

            myRef.setValue("Hello, Worl2d3!")
        }




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.actionSignOut){
            auth.signOut()
            Toast.makeText(applicationContext, "Session has been closed",Toast.LENGTH_LONG).show()
            goToLogin()
        }
        return true
    }
    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}