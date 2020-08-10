package com.example.vendingmachine.home

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.vendingmachine.R
import com.example.vendingmachine.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(){

    lateinit var viewModel: HomeViewModel
    lateinit var navController : NavController
    lateinit var sharedPreferences: SharedPreferences
    var coinCount = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        requireActivity().setTitle(getString(R.string.app_name))
        setHasOptionsMenu(true)
        sharedPreferences = requireActivity().getSharedPreferences("pref", 0)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.homeViewModel = viewModel
        binding.setLifecycleOwner(this)
        navController = findNavController()
        getCoinCount()

        viewModel.numberOfCoins.observe(viewLifecycleOwner, Observer {
            coinCount = it
            if (it > 0) {
                showCoin(binding)
            } else {
                hideCoinAndShowMessage(binding)
            }
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
    }

    private fun showCoin(binding: FragmentHomeBinding) {
        binding.noFundsText?.visibility = View.GONE
        binding.noFundsContainer?.visibility = View.GONE
        binding.noFundsHint?.visibility = View.GONE
        binding.coins.visibility = View.VISIBLE
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
            R.id.new_task -> {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToTasksFragment())
                saveCoinCount()
            }
        }
        return super.onOptionsItemSelected(item)
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