package com.amamode.realestatemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_estate_list.*

class EstateListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estate_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isTablet = resources.getBoolean(R.bool.isTablet)

        if (isTablet) {
            val tabletDetailNavHost =
                childFragmentManager.findFragmentById(R.id.tablet_detail_nav_container) as NavHostFragment
            displayFullLayout(tabletDetailNavHost)
            val action = EstateListFragmentDirections.goToDetailDest(4)
            tabletDetailNavHost.navController.navigate(action)
        } else {
            displaySingleLayout()
        }
    }

    private fun displaySingleLayout() {
        accountButton.setOnClickListener {
            val action = EstateListFragmentDirections.goToDetailDest(1)
            findNavController().navigate(action)
        }
        notificationButton.setOnClickListener {
            val action = EstateListFragmentDirections.goToDetailDest(2)
            findNavController().navigate(action)
        }
        settingButton.setOnClickListener {
            val action = EstateListFragmentDirections.goToDetailDest(3)
            findNavController().navigate(action)
        }
    }

    private fun displayFullLayout(tabletDetailNavHost: NavHostFragment) {
        accountButton.setOnClickListener {
            val action = EstateListFragmentDirections.goToDetailDest(1)
            tabletDetailNavHost.navController.navigate(action)
        }
        notificationButton.setOnClickListener {
            val action = EstateListFragmentDirections.goToDetailDest(2)
            tabletDetailNavHost.navController.navigate(action)
        }
        settingButton.setOnClickListener {
            val action = EstateListFragmentDirections.goToDetailDest(3)
            tabletDetailNavHost.navController.navigate(action)
        }
    }
}
