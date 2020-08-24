package com.example.vendingmachine.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.vendingmachine.databinding.AuthenticatingFragmentBinding

class AuthenticatingFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AuthenticatingFragmentBinding.inflate(inflater)

        return binding.root
    }
}