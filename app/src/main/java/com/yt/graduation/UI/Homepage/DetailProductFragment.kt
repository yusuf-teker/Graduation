package com.yt.graduation.UI.Homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.yt.graduation.databinding.FragmentDetailProductBinding
import com.yt.graduation.model.Product
import java.text.NumberFormat
import java.util.*


class DetailProductFragment : Fragment() {
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!
    var receivedProductInformations : Product? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            receivedProductInformations = arguments?.getParcelable("productInformation")
        }
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}