package com.venderino.vendingmachine.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.venderino.vendingmachine.R
import com.venderino.vendingmachine.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel : AuthViewModel by activityViewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentLoginBinding.inflate(layoutInflater)


        binding.signInButton.setOnClickListener {
            viewModel.setEmailAddress(binding.emailInput.editableText.toString())
            viewModel.setPassword(binding.passwordInput.editableText.toString())

        }

        viewModel.email_address.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.validateCredentials(it, viewModel.password.value.toString())
            }
        })

        viewModel.signInStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

//                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToAuthenticatingFragment())
            }
        })

        return binding.root
    }

}