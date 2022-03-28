package com.yt.graduation.UI.Account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yt.graduation.R
import com.yt.graduation.UI.Adapters.AllProductsAdapter
import com.yt.graduation.databinding.AddProductFragmentBinding
import com.yt.graduation.databinding.FragmentFavoriteProductsBinding


class   FavoriteProductsFragment : Fragment() {
    private var _binding: FragmentFavoriteProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoriteProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavoriteProductsViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteProductsBinding.inflate(inflater, container, false)

        binding.FavoriteRecyclerView.layoutManager = GridLayoutManager(context,2)

        val adapter =  AllProductsAdapter(ArrayList())
        binding.FavoriteRecyclerView.adapter = adapter

        viewModel.favoriteProducts.observe(viewLifecycleOwner){
            adapter.refreshData(it)
        }



        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
    }
}