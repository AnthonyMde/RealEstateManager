package com.amamode.realestatemanager.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_empty_detail_fragment.*
import org.jetbrains.anko.support.v4.toast

class EmptyDetailFragment : Fragment(R.layout.fragment_empty_detail_fragment) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        emptyViewAddEstateCta.setOnClickListener {
            toast("Création")
        }
    }
}
