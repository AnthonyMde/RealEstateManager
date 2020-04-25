package com.amamode.realestatemanager.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_estate_list.*

class EstateListFragment : Fragment(R.layout.fragment_estate_list) {
    var firstTime = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isTablet = resources.getBoolean(R.bool.isTablet)

        if (isTablet) {
            val tabletDetailNavHost =
                childFragmentManager.findFragmentById(R.id.tablet_detail_nav_container) as NavHostFragment
            displayFullLayout(tabletDetailNavHost)
        } else {
            displaySingleLayout()
        }
    }

    private fun displaySingleLayout() {
        estateRV.adapter = EstateAdapter(onEstateClick = { estate ->
            val action = EstateListFragmentDirections.goToDetailDest(estate)
            findNavController().navigate(action)
        })
    }

    private fun displayFullLayout(tabletDetailNavHost: NavHostFragment) {
        estateRV.adapter = EstateAdapter(onEstateClick = { estate ->
            if (firstTime) {
                firstTime = false
                val action =
                    EmptyDetailFragmentDirections.actionEmptyDetailFragmentToDetailDest(estate)
                tabletDetailNavHost.navController.navigate(action)
            } else {
                val action = EstateListFragmentDirections.goToDetailDest(estate)
                tabletDetailNavHost.navController.navigate(action)
            }
        })
    }
}
