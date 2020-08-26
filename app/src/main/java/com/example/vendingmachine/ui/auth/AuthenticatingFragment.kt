package com.example.vendingmachine.ui.auth

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.vendingmachine.databinding.AuthenticatingFragmentBinding
import com.example.vendingmachine.ui.MainActivity
import com.example.vendingmachine.ui.home.HomeViewModel
import com.example.vendingmachine.utils.Constants.SIGN_IN_CODE
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticatingFragment : Fragment(){

    private val viewModel : AuthViewModel by activityViewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AuthenticatingFragmentBinding.inflate(inflater)

        setUpAnimations(binding)

        viewModel.signInStatus.observe(viewLifecycleOwner, Observer {
            firebaseUser ->
            if(firebaseUser.isNullOrEmpty()){
                launchSignIn()
            }else{
                Toast.makeText(requireContext(), "Signed In as ${firebaseUser}", Toast.LENGTH_SHORT).show()
                val mainActivityIntent = Intent(requireContext(), MainActivity::class.java)
                startActivity(mainActivityIntent)
            }
        })

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_CODE) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                viewModel.getUserStatus()
            } else {


            }
        }
    }

    fun launchSignIn(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            SIGN_IN_CODE
        )

    }

    fun setUpAnimations(binding: AuthenticatingFragmentBinding) {
        val blueDotAnimation = ObjectAnimator.ofFloat(binding.blueDot, View.TRANSLATION_Y, -10f,10f)
        blueDotAnimation.repeatCount = ObjectAnimator.INFINITE
        blueDotAnimation.repeatMode = ObjectAnimator.REVERSE
        blueDotAnimation.start()

        val greenDotAnimation = ObjectAnimator.ofFloat(binding.greenDot, View.TRANSLATION_Y, 10f, -10f)
        greenDotAnimation.repeatCount = ObjectAnimator.INFINITE
        greenDotAnimation.repeatMode = ObjectAnimator.REVERSE
        greenDotAnimation.start()

        val yellowDotAnimation = ObjectAnimator.ofFloat(binding.yellowDot, View.TRANSLATION_Y, -10f,10f)
        yellowDotAnimation.repeatCount = ObjectAnimator.INFINITE
        yellowDotAnimation.repeatMode = ObjectAnimator.REVERSE
        yellowDotAnimation.start()
    }
}