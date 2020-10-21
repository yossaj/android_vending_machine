package com.venderino.vendingmachine.ui.home

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.venderino.vendingmachine.R
import com.venderino.vendingmachine.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(){

    private val viewModel : HomeViewModel by activityViewModels<HomeViewModel>()
    lateinit var navController : NavController
    lateinit var sharedPreferences: SharedPreferences
    var coinCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        sharedPreferences = requireActivity().getSharedPreferences("pref", 0)
        binding.homeViewModel = viewModel
        binding.setLifecycleOwner(this)
        navController = findNavController()
        getCoinCount()

        viewModel.numberOfCoins.observe(viewLifecycleOwner, Observer {
            coinCount = it
            if (it > 0) {
                showCoin(binding)
                binding.coinCountDisplay?.setText("x${it}")
            } else {
                hideCoinAndShowMessage(binding)
            }
        })

        viewModel.clicked.observe(viewLifecycleOwner, Observer {
            if (it){
                viewModel.resetResponse()
                viewModel.resetClicked()
                if (viewModel.balance >= 100 && !viewModel.apiKey.value.isNullOrBlank()) {
                    navController.navigate(
                        HomeFragmentDirections.actionHomeFragmentToHomeDialogFragment()
                    )
                    viewModel.updateDisplayedBalanceUponSale()

                } else {
                    insufficentFundsSnackbar(binding)
                }
            }
        })
        binding.foxButton.setOnClickListener{
            Snackbar.make(it, "OUT OF STOCK", Snackbar.LENGTH_SHORT).show()
        }

        binding.coinDragDestination.setOnDragListener(dragListener)
        binding.coins.setOnLongClickListener(){
            val dragShadowBuilder = View.DragShadowBuilder(it)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(null, dragShadowBuilder, it, 0)
                it.visibility =  View.INVISIBLE
            }else{
                viewModel.addToBalanceAndConvertToString()
                viewModel.reduceCoinCountByOne()
            }
            true
        }
        return binding.root
    }

    private fun hideCoinAndShowMessage(binding: FragmentHomeBinding) {
        binding.noFundsText?.visibility = View.VISIBLE
        binding.noFundsContainer?.visibility = View.VISIBLE
        binding.noFundsHint?.visibility = View.VISIBLE
        binding.coins.visibility = View.GONE
        binding.coinCountDisplay?.visibility = View.GONE
    }

    private fun showCoin(binding: FragmentHomeBinding) {
        binding.noFundsText?.visibility = View.GONE
        binding.noFundsContainer?.visibility = View.GONE
        binding.noFundsHint?.visibility = View.GONE
        binding.coins.visibility = View.VISIBLE
        binding.coinCountDisplay?.visibility = View.VISIBLE
    }

    private fun getCoinCount() {
        val coinCount = sharedPreferences.getInt(getString(R.string.coin_count_key), 0)
        viewModel._numberOfCoins.value = coinCount
    }

    val dragListener = View.OnDragListener{view, event ->
        when(event.action){
            DragEvent.ACTION_DRAG_ENTERED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                if(coinCount > 0) {
                    coins.visibility = View.VISIBLE
                }
                true
            }
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DROP -> {
                coins.visibility = View.VISIBLE
                viewModel.addToBalanceAndConvertToString()
                viewModel.reduceCoinCountByOne()

                true
            }
            else -> true
        }
    }

    fun insufficentFundsSnackbar(binding: FragmentHomeBinding) {
        Snackbar.make(binding.root, getString(R.string.no_funds), Snackbar.LENGTH_LONG)
            .setActionTextColor(resources.getColor(R.color.colorPrimaryDark))
            .setTextColor(resources.getColor(R.color.colorPrimaryDark))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.task_yellow))
            .show()
    }

    fun saveCoinCount(){
        viewModel.numberOfCoins.value?.let {
            sharedPreferences.edit().putInt(getString(R.string.coin_count_key), it).apply()
        }
    }

    override fun onPause() {
        saveCoinCount()
        super.onPause()
    }


}