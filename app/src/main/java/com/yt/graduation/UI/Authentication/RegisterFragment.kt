package com.yt.graduation.UI.Authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yt.graduation.R
import com.yt.graduation.databinding.FragmentRegisterBinding
import com.yt.graduation.model.User
import com.yt.graduation.util.Resource
import java.util.*
import kotlin.collections.ArrayList

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.registerButton.setOnClickListener() {
            register()
        }
        binding.goToLogin.setOnClickListener() {
            goToLogin()
        }


        viewModel.userRegistrationStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.registerProgressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.registerProgressBar.isVisible = false
                    Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    goToMain()
                }
                is Resource.Error -> {
                    binding.registerProgressBar.isVisible = false
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun goToMain() {
        findNavController().navigate(R.id.action_registerFragment_to_allProductsFragment)
    }

    private fun goToLogin() {
        findNavController().navigate(R.id.loginFragment)
    }

    private fun register() {
        val user = User()
        binding.apply {
            user.email = registerMail.text.toString()
            user.name = registerName.text.toString()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            //registerProgressBar.visibility= View.VISIBLE
            user.registrationDate = Calendar.getInstance().time.toString()
            user.favoriteProducts = ArrayList<String>()
        }
        viewModel.register(user,binding.registerPassword.text.toString(),binding.registerPasswordAgain.text.toString())

    }
}