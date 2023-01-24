package com.example.asteroidradar.main

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.asteroidradar.R
import com.example.asteroidradar.databinding.FragmentMainBinding

@Suppress("DEPRECATION")
@RequiresApi(Build.VERSION_CODES.O)
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireActivity().application
        ViewModelProvider(this, MainViewModelFactory(activity, isConnected()))[MainViewModel::class.java]
    }

    private fun isConnected(): Boolean {
        val manager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo?.isConnected ?: false
    }

    private lateinit var adapter: AsteroidsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        adapter = AsteroidsAdapter(AsteroidsAdapter.AsteroidClickListener{
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailFragment(it)
            )
        })
        viewModel.todayAsteroids.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        binding.asteroidRecycler.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.view_week_asteroids -> adapter.submitList(viewModel.weekAsteroid.value)
                    R.id.view_saved_asteroids -> adapter.submitList(viewModel.allAsteroid.value)
                    R.id.view_today_asteroids -> adapter.submitList(viewModel.todayAsteroids.value)
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

}