package com.yt.graduation.UI.Authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yt.graduation.R
import com.yt.graduation.UI.Homepage.MainActivity
import com.yt.graduation.databinding.FragmentLoginBinding
import com.yt.graduation.util.Resource

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener() {
            login()
        }
        binding.goToRegister.setOnClickListener() {
            goToRegister()
        }
        viewModel.userSignUpStatus.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    binding.loginProgressBar.visibility= View.GONE
                    goToMain()
                }
                is Resource.Loading -> {
                    binding.loginProgressBar.visibility= View.VISIBLE
                }
                is Resource.Error -> {
                    binding.loginProgressBar.visibility= View.GONE
                    Toast.makeText(context, "Giriş Yapılamadı", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
    }
    private fun login() {
        binding.apply {
            viewModel.login(loginMail.text.toString(),loginPassword.text.toString())
        }
    }

    private fun goToRegister() {
       findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }
    private fun goToMain() {
        findNavController().navigate(R.id.action_loginFragment_to_allProductsFragment)
    }

}