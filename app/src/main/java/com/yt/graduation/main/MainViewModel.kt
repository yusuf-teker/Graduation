package com.yt.graduation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

var auth: FirebaseAuth = FirebaseAuth.getInstance()

class MainViewModel: ViewModel() {
    private var _signStatus = MutableLiveData<Boolean>()
    val signStatus: LiveData<Boolean>
        get() = _signStatus

    fun isSigned(){
        Log.d("aa","signStatus changed")
        _signStatus.postValue(auth.currentUser!=null)
    }
}