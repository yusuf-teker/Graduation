package com.yt.graduation.repository



import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.yt.graduation.util.Resource
import com.yt.graduation.util.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class   LoginRepository {

    private var auth: FirebaseAuth= FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO){
            safeCall {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result)
            }
        }
    }
}