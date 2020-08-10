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

        val spinnerValues = EstateType.values().map { getString(it.nameRes) }
            .toMutableList()
        spinnerValues.add(0, getString(R.string.filter_type_all_estate))
        spinnerValues.removeAt(spinnerValues.size - 1)
        filterEstateTypeSpinner.setItems(spinnerValues)
    }

    private fun getFilterData(): FilterEntity {
        val ownerText = filterEstateOwner.text.toString().trim()
        return FilterEntity(
            owner = if (ownerText.isNotEmpty()) ownerText else null,
            type = getFilterEstateType(filterEstateTypeSpinner.selectedIndex)
        )
    }

    private fun getFilterEstateType(position: Int): EstateType? =
        when (position) {
            0 -> null
            1 -> EstateType.HOUSE
            2 -> EstateType.APARTMENT
            3 -> EstateType.LOFT
            4 -> EstateType.DUPLEX
            5 -> EstateType.VILLA
            else -> EstateType.UNKNOWN
        }
}
