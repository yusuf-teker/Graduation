package com.yt.graduation.UI.Homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yt.graduation.repository.DetailProductRepository
import com.yt.graduation.util.FirebaseResultListener

class DetailProductsViewModel: ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean>
        get() = _isFavorite

    private val repository = DetailProductRepository()

    fun addOrRemoveFavorites(productKey: String){
        repository.addToFavorites(productKey, resultListener = object : FirebaseResultListener {
            override fun onSuccess(isSuccess: Boolean) {
                _isFavorite.postValue(isSuccess)
            }
        })
    }
    fun isFavorite(productKey: String){
        repository.isFavorite(productKey, resultListener =  object : FirebaseResultListener {
            override fun onSuccess(isSuccess: Boolean) {
                _isFavorite.postValue(isSuccess)
            }
        })
    }




}