package com.amamode.realestatemanager.ui.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.databinding.FragmentEstateCreationBinding
import com.amamode.realestatemanager.ui.EstateViewModel
import kotlinx.android.synthetic.main.fragment_estate_creation.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateCreationFragment : Fragment() {
    private val estateViewModel: EstateViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingLayout = DataBindingUtil.inflate<FragmentEstateCreationBinding>(
            inflater,
            R.layout.fragment_estate_creation,
            container,
            false
        )

        bindingLayout.lifecycleOwner = this
        bindingLayout.viewmodel = estateViewModel
        return bindingLayout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        creationCTA.setOnClickListener {
            estateViewModel.createEstate()
            findNavController().popBackStack()
        }

        estateViewModel.formMediator.observe(viewLifecycleOwner, Observer {
            creationCTA.isEnabled = it
        })
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(estateCreationToolbar as Toolbar)
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.estate_form_toolbar_title)

        }
    }
}
