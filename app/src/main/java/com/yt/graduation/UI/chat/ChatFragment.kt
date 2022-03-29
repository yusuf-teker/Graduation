package com.yt.graduation.UI.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yt.graduation.UI.Adapters.ChatAdapter
import com.yt.graduation.databinding.FragmentChatBinding
import com.yt.graduation.model.Message


class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ChatViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel  = ViewModelProvider(this).get(ChatViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        var adapter = ChatAdapter(ArrayList<Message>())
        recyclerView.adapter = adapter

        viewModel.messages.observe(viewLifecycleOwner){
            adapter.refreshMessages(it)
        }




        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshMessages()
    }

}