package com.yt.graduation.UI.chat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yt.graduation.R
import com.yt.graduation.UI.Adapters.ChatAdapter
import com.yt.graduation.data.SettingsDataStore
import com.yt.graduation.databinding.FragmentChatBinding
import com.yt.graduation.main.MainActivity
import com.yt.graduation.model.Message
import com.yt.graduation.model.User
import java.time.LocalDateTime
import java.util.*

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ChatViewModel

    private lateinit var receiverUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO deprecated
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        viewModel  = ViewModelProvider(this).get(ChatViewModel::class.java)
        if (arguments != null) {
            receiverUser = arguments?.getParcelable("receiverUser")!!
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)


        /* bind Choosen User Information-------------------------------------*/
        if (receiverUser.image.isEmpty() || receiverUser.image=="default"){
            binding.receiverUserImage.setImageResource(com.yt.graduation.R.drawable.defaultuser)
        }else{
            Glide.with(requireContext())
                .load(receiverUser.image) // image url
                .into(binding.receiverUserImage)
        }
        binding.receiverUserName.text = receiverUser.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }  /* -------------------------------------------------------------*/


        /* Recycler View And Adapter --------------------------*/
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        var adapter = ChatAdapter(ArrayList<Message>(),receiverUser.uid,requireContext())
        recyclerView.adapter = adapter /* ---------------------*/

        //Observe messages
        viewModel.messages.observe(viewLifecycleOwner){
            adapter.refreshMessages(it)
            recyclerView.scrollToPosition(adapter.itemCount-1)
        }


        /* PICK PHOTO and Bind It------------------------------------------------------------------*/
        var selectedImageUri: Uri? = null
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                selectedImageUri = intent?.data
                Log.d("chat setImageURI",selectedImageUri.toString())

            /*    val photoRef: StorageReference = mChatPhotosStorageReference!!.child(
                    selectedImageUri!!.lastPathSegment!!
                )*/
              binding.photoPickerButton.setImageURI(selectedImageUri)
            }
        }

        binding.photoPickerButton.setOnClickListener(){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startForResult.launch(Intent.createChooser(intent, "Complete action using"))
        } /*----------------------------------------------------------------------------------------*/

        binding.messageEditText.setOnClickListener{
            recyclerView.scrollToPosition(adapter.itemCount-1)
        }

        // Send new messages
        binding.sendMessageButton.setOnClickListener{
             val message: Message = Message()
             val messageContent = binding.messageEditText.text.toString()
                if (selectedImageUri!=null)
                message.messageImageUrl = selectedImageUri.toString()
                message.messageText = messageContent
                message.timeToSend = LocalDateTime.now().toString()
                message.receiverId = receiverUser.uid

            binding.messageEditText.text.clear()
            viewModel.sendMessage(message)


        }

        binding.navigateUpButton.setOnClickListener{
            it.findNavController().navigateUp()
        }

        viewModel.wallpaper.observe(viewLifecycleOwner){
            if (it!="default" && it.isNotBlank()){
                binding.backgroundImage.setImageURI(it.toUri())
            }else{
                binding.backgroundImage.setImageResource(R.drawable.chat_background)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshMessages(receiverUser.uid)
        (requireActivity() as MainActivity).supportActionBar!!.hide()
        binding.messageEditText.requestFocus()
    }

    override fun onDetach() {
        super.onDetach()
        (requireActivity() as MainActivity).supportActionBar!!.show()

    }


}



