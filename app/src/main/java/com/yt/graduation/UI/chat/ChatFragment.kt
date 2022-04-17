package com.yt.graduation.UI.chat

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.yt.graduation.R
import com.yt.graduation.UI.Adapters.ChatAdapter
import com.yt.graduation.databinding.FragmentChatBinding
import com.yt.graduation.main.MainActivity
import com.yt.graduation.main.auth
import com.yt.graduation.model.Message
import com.yt.graduation.model.User
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


private const val RC_PHOTO_PICKER = 2

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
            binding.receiverUserImage.setImageResource(R.drawable.defaultuser)
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
                Log.d("chat intet.data",selectedImageUri.toString())

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

