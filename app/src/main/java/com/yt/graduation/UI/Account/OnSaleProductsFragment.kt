package com.yt.graduation.UI.Account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yt.graduation.UI.Adapters.OnSaleProductsAdapter
import com.yt.graduation.databinding.FragmentOnSaleProductsBinding
import com.yt.graduation.model.Product


class OnSaleProductsFragment : Fragment() {
    private var _binding: FragmentOnSaleProductsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView : RecyclerView
    private lateinit var viewModel: OnSaleProductsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel   = ViewModelProvider(this).get(OnSaleProductsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnSaleProductsBinding.inflate(inflater, container, false)

        recyclerView = binding.OnSaleRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context,2)
        val adapter = OnSaleProductsAdapter(ArrayList<Product>(),viewModel)
        recyclerView.adapter = adapter
        viewModel.productList.observe(viewLifecycleOwner){ products ->
            adapter.refreshData(products)
        }


        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        viewModel.refreshProducts()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}