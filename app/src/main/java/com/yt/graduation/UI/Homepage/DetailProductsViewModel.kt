package com.yt.graduation.UI.Homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yt.graduation.model.Product
import com.yt.graduation.model.User
import com.yt.graduation.repository.DetailProductRepository
import com.yt.graduation.util.FirebaseResultListener

class DetailProductsViewModel: ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean>
        get() = _isFavorite

    private val _productOwnerName = MutableLiveData<String>()
    val productOwnerName :LiveData<String>
        get() = _productOwnerName

    private val _productOwnerImage = MutableLiveData<String>()
    val productOwnerImage :LiveData<String>
        get() = _productOwnerImage



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

    fun getOwnerInfo(ownerId: String){
        repository.getOwnerInfo(ownerId, callback = object : DetailProductRepository.OnDataReceiveCallback{
            override fun onDataReceived(ownerInfo: ArrayList<String>) {
                _productOwnerName.postValue(ownerInfo[0])
                _productOwnerImage.postValue(ownerInfo[1])
            }

        })
    }


}