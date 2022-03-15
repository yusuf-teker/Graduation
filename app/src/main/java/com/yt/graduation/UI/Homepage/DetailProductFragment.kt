package com.yt.graduation.UI.Homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.yt.graduation.databinding.FragmentDetailProductBinding
import java.text.NumberFormat
import java.util.*


class DetailProductFragment : Fragment() {
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!
    var receivedProductInformations : List<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            receivedProductInformations = arguments?.getString("productInformation")?.split("*")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)

        if (receivedProductInformations != null){
            binding.productName.text =  receivedProductInformations!![0]

            val number = receivedProductInformations!![1].toInt()
            val COUNTRY = "TR"
            val LANGUAGE = "tr"
            val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number)

            binding.productPrice.text = str
            binding.productDescription.text = receivedProductInformations!![2]
            binding.productUploadDate.text = receivedProductInformations!![4].subSequence(0..9 )
            Toast.makeText(context, receivedProductInformations!![6],Toast.LENGTH_SHORT).show()
            Glide.with(binding.productImage.context)
                .load(receivedProductInformations!![6]) // image url
                .into(binding.productImage)




        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}