package com.yt.graduation.UI.Homepage

import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.yt.graduation.R
import com.yt.graduation.UI.Adapters.AllProductsAdapter
import com.yt.graduation.databinding.FragmentAllProductsBinding
import com.yt.graduation.repository.AllProductsRepository
import kotlin.getValue


class AllProductsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{
    private var _binding: FragmentAllProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : AllProductsAdapter

    private val viewModel: AllProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshProducts()

        viewModel.productList.observe(this){
            recyclerView = binding.HomepageRecyclerView
            recyclerView.layoutManager = GridLayoutManager(context,2)
            adapter = AllProductsAdapter(it)
            recyclerView.adapter = adapter
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            _binding = FragmentAllProductsBinding.inflate(inflater, container, false)
            return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Before fetchind data //create empty list
        recyclerView = binding.HomepageRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context,2)
        adapter = AllProductsAdapter(ArrayList())
        recyclerView.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener(this)
        binding.swipeRefresh.setColorSchemeResources(
            R.color.primaryColor,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark)


    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshProducts()
    }
    override fun onRefresh() {
       viewModel.refreshProducts()
        binding.swipeRefresh.isRefreshing = false;
    }


}

