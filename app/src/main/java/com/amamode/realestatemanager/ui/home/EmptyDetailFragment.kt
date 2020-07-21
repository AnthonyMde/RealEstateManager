package com.amamode.realestatemanager.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.ui.EstateViewModel
import kotlinx.android.synthetic.main.fragment_empty_detail_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EmptyDetailFragment : Fragment(R.layout.fragment_empty_detail_fragment) {
    private val estateViewModel: EstateViewModel by sharedViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        emptyViewAddEstateCta.setOnClickListener {
            estateViewModel.clearFormerCreationData()
            activity?.findNavController(R.id.main_nav_container)
                ?.navigate(R.id.goToEstateCreation)
        }
    }
}
