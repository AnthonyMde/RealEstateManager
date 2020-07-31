package com.amamode.realestatemanager.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.FilterEntity
import com.amamode.realestatemanager.ui.creation.EstateType
import kotlinx.android.synthetic.main.activity_filter.*

const val FILTER_RESULT_CODE = 1026
const val FILTER_DATA_EXTRA = "FILTER_DATA"

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        filterEstateCTA.setOnClickListener {
            val data = getFilterData()
            setResult(FILTER_RESULT_CODE, Intent().putExtra(FILTER_DATA_EXTRA, data))
            finish()
        }

        filterEstateTypeSpinner.setItems(EstateType.values().map { getString(it.nameRes) })
    }

    private fun getFilterData(): FilterEntity = FilterEntity(
        owner = filterEstateOwner.text.toString(),
        type = getEstateType(filterEstateTypeSpinner.selectedIndex)
    )
}

fun getEstateType(position: Int): EstateType =
    when (position) {
        0 -> EstateType.HOUSE
        1 -> EstateType.APARTMENT
        2 -> EstateType.LOFT
        3 -> EstateType.DUPLEX
        4 -> EstateType.VILLA
        else -> EstateType.UNKNOWN
    }
