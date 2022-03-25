package com.yt.graduation.UI.Account


import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.yt.graduation.model.Product
import com.yt.graduation.repository.AddProductRepository
import com.yt.graduation.util.FirebaseResultListener
import kotlin.collections.ArrayList

class AddProductViewModel : ViewModel() {
    private val repository = AddProductRepository()
    var auth: FirebaseAuth = repository.auth
     var  addProductFragment: AddProductFragment? = null

    //make categories observable //it is assigned when view model first created
    private val _categories = MutableLiveData<ArrayList<String>>()
    val categories: LiveData<ArrayList<String>> = _categories

    private val _isAdded = MutableLiveData<Boolean>()
    val isAdded: LiveData<Boolean> = _isAdded

    fun setIsAdded(boolean: Boolean){
        _isAdded.postValue(boolean)
    }

    fun getCategories(): ArrayList<String> {
        return repository.getCategories(object : AddProductRepository.OnDataReceiveCallback {
            override fun onDataReceived(categories: ArrayList<String>) {
                //Spinner
                _categories.postValue(categories)
            }
        })
    }

    //Listener
     fun addProduct(product: Product, onCompletedListener: FirebaseResultListener) {
        _isAdded.postValue(false)
         //View Model View Model arası
         //var viewModel2 = ViewModelProvider(addProductFragment!!).get(AllProductsViewModel::class.java)
         //  viewModel2.setProductList(ArrayList()) //Referans ile iletişim kuruldu

         if (!validateProduct(product)){
             _isAdded.postValue(true)
        }else repository.addProduct(product,onCompletedListener)

    }

    private fun validateProduct(product: Product): Boolean {
        val isValid = (product.productName.length in 2..20 && product.productPrice in 0..1000000 && product.productDescription.length < 140)
        return isValid
    }

}