package com.yt.graduation.UI.chat

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yt.graduation.R
import com.yt.graduation.UI.Adapters.UsersAdapter
import com.yt.graduation.data.SettingsDataStore
import com.yt.graduation.databinding.FragmentUsersBinding
import com.yt.graduation.model.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UsersFragment : Fragment() {
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: UsersViewModel

    private lateinit var SettingsDataStore: SettingsDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        SettingsDataStore = SettingsDataStore(requireContext())
        lifecycleScope.launch {
            SettingsDataStore.preferenceFlow.collectLatest {
                setBackground(it)
            }
        }
        //TODO deprecated
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        viewModel = ViewModelProvider(this).get(UsersViewModel::class.java)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        var adapter = UsersAdapter(ArrayList<User>(), requireContext())
        recyclerView.adapter = adapter

        viewModel.usersList.observe(viewLifecycleOwner){
            adapter.refreshData(it)
        }
        binding.searchUserText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }

        })

    /*    viewModel.wallpaper.observe(viewLifecycleOwner){
            if (it!="default" && it.isNotBlank()){
                binding.backgroundImage.setImageURI(it.toUri())
            }else{
                binding.backgroundImage.setImageResource(R.drawable.chat_background)
            }
        }
*/


        return binding.root
    }


    fun setBackground(wallpaperString: String){
        if (wallpaperString!="default" && wallpaperString.isNotBlank()){
            binding.backgroundImage.setImageURI(wallpaperString.toUri())
        }else{
            binding.backgroundImage.setImageResource(R.drawable.chat_background)
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.refreshSpeeches()
    }

}