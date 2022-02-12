package com.yt.graduation.UI.Authentication

import androidx.lifecycle.ViewModel
import com.yt.graduation.model.User

class RegisterViewModel : ViewModel() {


    val user = User()
    fun validateRegisteration(name: String, email: String,password1:String, password2: String):Boolean{
        return email.takeLast(11)=="@itu.edu.tr"  && name.length in 2..25 && password1.isNotEmpty() && password1 == password2
    }
}