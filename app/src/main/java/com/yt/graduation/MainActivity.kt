package com.yt.graduation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        if ( auth.currentUser != null ){
            val database = Firebase.database
            val myRef = database.getReference("message")

            myRef.setValue("Hello, Worl2gd!")

            myRef.setValue("Hello, Worl2d3!")
        }




    }
}