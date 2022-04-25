package com.yt.graduation.UI.Homepage

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.yt.graduation.R
import com.yt.graduation.UI.Adapters.AllProductsAdapter
import com.yt.graduation.databinding.FragmentAllProductsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AllProductsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{
    private var _binding: FragmentAllProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView : RecyclerView

    private lateinit var viewModel: AllProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel   = ViewModelProvider(this).get(AllProductsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllProductsBinding.inflate(inflater, container, false)

        recyclerView = binding.HomepageRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context,2)
        val adapter =  AllProductsAdapter(ArrayList())
        recyclerView.adapter = adapter
        viewModel.productList.observe(viewLifecycleOwner){ products ->
            GlobalScope.launch(Dispatchers.Main ) {
                products.let {
                    adapter.refreshData(it)
                }
            }
        }
        viewModel.signStatus.observe(viewLifecycleOwner){
            if (!it) findNavController().navigate(R.id.loginFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        viewModel.isSigned()
    }
    override fun onRefresh() {
       viewModel.refreshProducts()
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

