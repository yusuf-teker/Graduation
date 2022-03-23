package com.yt.graduation.UI.Account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yt.graduation.R
import com.yt.graduation.databinding.AddProductFragmentBinding
import com.yt.graduation.databinding.FragmentFavoriteProductsBinding


class   FavoriteProductsFragment : Fragment() {
    private var _binding: FragmentFavoriteProductsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteProductsBinding.inflate(inflater, container, false)



        return binding.root
    }


}