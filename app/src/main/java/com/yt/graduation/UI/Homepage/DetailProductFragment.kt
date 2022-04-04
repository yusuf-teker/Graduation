package com.yt.graduation.UI.Homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.yt.graduation.R
import com.yt.graduation.UI.chat.UsersFragmentDirections
import com.yt.graduation.databinding.FragmentDetailProductBinding
import com.yt.graduation.model.Product
import com.yt.graduation.model.User
import com.yt.graduation.util.FirebaseResultListener
import com.yt.graduation.util.OnDataReceivedCallback
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class DetailProductFragment : Fragment() {
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!
    var receivedProductInformations : Product? = null
    private lateinit var viewModel : DetailProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            receivedProductInformations = arguments?.getParcelable("productInformation")
        }
        viewModel = ViewModelProvider(this).get(DetailProductsViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)

        if (receivedProductInformations != null){
            binding.productName.text =  receivedProductInformations!!.productName


            val number = receivedProductInformations!!.productPrice
            val COUNTRY = "TR"
            val LANGUAGE = "tr"
            val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number)

            binding.apply {
                productPrice.text = str
                productDescription.text = receivedProductInformations!!.productDescription
                productUploadDate.text = receivedProductInformations!!.productUploadDate.subSequence(0..9 )
            }

            Glide.with(binding.productImage.context)
                .load(receivedProductInformations!!.productImage) // image url
                .into(binding.productImage)
        }

        viewModel.isFavorite.observe(viewLifecycleOwner){
            if (it){
                binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_black_36)
            }else
                binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_black_36)
        }
        viewModel.productOwnerName.observe(viewLifecycleOwner){
            binding.ownerName.text = it
        }
        viewModel.productOwnerImage.observe(viewLifecycleOwner){
            if ( !(it.equals("default") || it.isEmpty())){
                activity?.let { activity->
                    Glide.with(activity)
                        .load(it) // image url
                        .circleCrop()
                        .into(binding.ownerImage)
                }
            }else{
                binding.ownerImage.setImageResource(R.drawable.defaultuser)
            }
        }


        binding.favoriteButton.setOnClickListener {
            viewModel.addOrRemoveFavorites(productKey = receivedProductInformations!!.productKey.toString(), firebaseResultListener = object : FirebaseResultListener{
                override fun onSuccess(isSuccess: Boolean) {
                    Toast.makeText(context, receivedProductInformations!!.productName+" added successfully",Toast.LENGTH_SHORT).show()
                }

            })
        }

        binding.ownerImage.setOnClickListener{
            goToOwnerChat()
        }
        binding.ownerName.setOnClickListener{
            goToOwnerChat()
        }



        return binding.root
    }

    override fun onResume() {
        super.onResume()
        receivedProductInformations!!.productKey?.let { viewModel.isFavorite(it) }
        viewModel.getOwnerInfo(receivedProductInformations!!.productOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun goToOwnerChat() {
        val user = User()
        user.image = viewModel.productOwnerImage.value.toString()
        user.name = viewModel.productOwnerName.value.toString()
        user.uid = viewModel.productOwnerID.value.toString()

        val action = DetailProductFragmentDirections.actionDetailProductFragmentToChatFragment(user)
        findNavController().navigate(action)
    }

}