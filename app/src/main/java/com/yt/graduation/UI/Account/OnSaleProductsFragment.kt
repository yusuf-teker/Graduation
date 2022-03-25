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
import com.yt.graduation.databinding.FragmentOnSaleProductsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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

        viewModel.productList.observe(viewLifecycleOwner){ products ->
            GlobalScope.launch(Dispatchers.Main ) {
                products.let {
                    recyclerView.adapter = AllProductsAdapter(it) //AllProductsAdapter is enough for now
                }
            }
        }


        // Inflate the layout for this fragment
        return  return binding.root
    }


    override fun onResume() {
        super.onResume()
        viewModel.getOnSaleProducts()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}