package com.venderino.vendingmachine.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.venderino.vendingmachine.R
import com.venderino.vendingmachine.databinding.FragmentSettingsBinding
import com.venderino.vendingmachine.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSettingsBinding.inflate(inflater)

        binding.signOutBtn.setOnClickListener {
            removeSharedPrefUsername()
            viewModel.signOut()
            viewModel.getUser()
        }

        viewModel.currentUser.observe(viewLifecycleOwner, Observer {
            if(it == null){
                val authIntent = Intent(requireContext(), AuthActivity::class.java)
                ActivityCompat.finishAffinity(requireActivity())
                startActivity(authIntent)
            }else{
                binding.userName.setText("Username: ${it.displayName}")
                binding.userEmail.setText("Email: ${it.email}")
            }
        })

        return binding.root
    }

    fun removeSharedPrefUsername(){
        val sharedPreferences = requireActivity().getSharedPreferences("pref", 0)
        sharedPreferences.edit().remove(getString(R.string.username_key)).apply()
    }

}