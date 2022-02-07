package com.yt.graduation.Settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yt.graduation.R
import com.yt.graduation.databinding.ActivityRegisterBinding
import com.yt.graduation.databinding.ActivitySettingsBinding

private lateinit var binding: ActivitySettingsBinding
class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (auth.currentUser!=null){
            auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser!!.uid
            database = Firebase.database


            //Read From Database
            val dbRef = database.reference.child("User").child(userId)

            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user_name = dataSnapshot.child("name").value.toString() //User içindeki userId içindeki name
                    val user_image = dataSnapshot.child("name").value.toString()

                    binding.settingsUserName.setText(user_name)


                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            dbRef.addValueEventListener(postListener)
        }

    }
}