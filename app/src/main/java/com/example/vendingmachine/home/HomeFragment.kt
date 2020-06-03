package com.example.vendingmachine.home

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.vendingmachine.R
import com.example.vendingmachine.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.annotations.NotNull

class HomeFragment : Fragment(){

    lateinit var viewModel: HomeViewModel
    lateinit var navController : NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)

        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.homeViewModel = viewModel
        binding.setLifecycleOwner(this)
        navController = findNavController()
        getCoinCount()

        viewModel.numberOfCoins.observe(viewLifecycleOwner, Observer {
            coinDisplaySwitch(it, binding)
        })

        viewModel.apiKey.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (viewModel.balance >= 100) {
                    navController.navigate(
                        HomeFragmentDirections.actionHomeFragmentToHomeDialogFragment(
                            it
                        )
                    )
                    viewModel.resetAPIKey()
                    viewModel.updateDisplayedBalanceUponSale()

                } else {
                    insufficentFundsSnackbar(binding)
                }
            }
        })

        binding.coinDragDestination.setOnDragListener(dragListener)
        binding.coins.setOnLongClickListener(){
            val dragShadowBuilder = View.DragShadowBuilder(it)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(null, dragShadowBuilder, it, 0)
            }
            it.visibility =  View.INVISIBLE
            true
        }

        return binding.root
    }

    private fun getCoinCount() {
        val coinCount = requireActivity()
            .getSharedPreferences("pref", 0)
            .getInt(getString(R.string.coin_count_key), 0)
        viewModel._numberOfCoins.value = coinCount
    }

    private fun coinDisplaySwitch( it: Int,  binding: FragmentHomeBinding) {
        if (it > 0) {
            binding.noFundsText?.visibility = View.GONE
            binding.noFundsContainer?.visibility = View.GONE
            binding.noFundsHint?.visibility = View.GONE
            binding.coins.visibility = View.VISIBLE

        } else {
            binding.noFundsText?.visibility = View.VISIBLE
            binding.noFundsContainer?.visibility = View.VISIBLE
            binding.noFundsHint?.visibility = View.VISIBLE
            binding.coins.visibility = View.GONE
        }
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
                coins.visibility = View.VISIBLE
                true
            }
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DROP -> {
                viewModel.addToBalanceAndConvertToString()
                coins.visibility = View.VISIBLE
                true
            }
            else -> true
        }
    }

    fun insufficentFundsSnackbar(binding: FragmentHomeBinding) {
        Snackbar.make(binding.root, "Insufficient Funds", Snackbar.LENGTH_LONG)
            .setActionTextColor(resources.getColor(R.color.colorPrimaryDark))
            .setTextColor(resources.getColor(R.color.colorPrimaryDark))
            .setBackgroundTint(resources.getColor(R.color.colorAccent))
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.new_task -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToTasksFragment())
        }
        return super.onOptionsItemSelected(item)
    }



}