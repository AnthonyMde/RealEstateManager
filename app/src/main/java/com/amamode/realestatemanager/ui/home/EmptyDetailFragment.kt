package com.amamode.realestatemanager.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_empty_detail_fragment.*

class EmptyDetailFragment : Fragment(R.layout.fragment_empty_detail_fragment) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        emptyViewAddEstateCta.setOnClickListener {
            activity?.findNavController(R.id.main_nav_container)
                ?.navigate(R.id.action_list_dest_to_estateCreationFragment)
        }
    }
}
