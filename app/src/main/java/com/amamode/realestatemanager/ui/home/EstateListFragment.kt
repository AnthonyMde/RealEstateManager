package com.amamode.realestatemanager.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.ui.EstateViewModel
import kotlinx.android.synthetic.main.fragment_estate_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateListFragment : Fragment(R.layout.fragment_estate_list) {
    private val estateViewModel: EstateViewModel by viewModel()
    private var firstTime = true
    private lateinit var adapter: EstateListAdapter

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        estateViewModel.estateEntityList.observe(viewLifecycleOwner, Observer {
            adapter.setEstateList(it)
        })
    }

    private fun displaySingleLayout() {
        adapter =
            EstateListAdapter(onEstateClick = { estate ->
                val action =
                    EstateListFragmentDirections.goToDetailDest(
                        estate
                    )
                findNavController().navigate(action)
            })

        estateRV.adapter = adapter
        addEstateFab.setOnClickListener {
            findNavController().navigate(R.id.action_list_dest_to_estateCreationFragment)
        }
    }

    private fun displayFullLayout(tabletDetailNavHost: NavHostFragment) {
        adapter =
            EstateListAdapter(onEstateClick = { estate ->
                if (firstTime) {
                    firstTime = false
                    val action =
                        EmptyDetailFragmentDirections.actionEmptyDetailFragmentToDetailDest(
                            estate
                        )
                    tabletDetailNavHost.navController.navigate(action)
                } else {
                    val action =
                        EstateListFragmentDirections.goToDetailDest(
                            estate
                        )
                    tabletDetailNavHost.navController.navigate(action)
                }
            })
        estateRV.adapter = adapter
    }
}
