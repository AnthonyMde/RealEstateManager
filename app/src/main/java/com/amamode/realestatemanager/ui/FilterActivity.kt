package com.amamode.realestatemanager.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.FilterEntity
import com.amamode.realestatemanager.ui.creation.EstateType
import kotlinx.android.synthetic.main.activity_filter.*
import java.text.SimpleDateFormat
import java.util.*

const val FILTER_RESULT_CODE = 1026
const val FILTER_DATA_EXTRA = "FILTER_DATA"

class FilterActivity : AppCompatActivity() {
    private val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        filterEstateCTA.setOnClickListener {
            val data = getFilterData()
            setResult(FILTER_RESULT_CODE, Intent().putExtra(FILTER_DATA_EXTRA, data))
            finish()
        }

        val spinnerTypeValues = EstateType.values().map { getString(it.nameRes) }
            .toMutableList()
        spinnerTypeValues.add(0, getString(R.string.filter_type_all_estate))
        spinnerTypeValues.removeAt(spinnerTypeValues.size - 1)
        filterEstateTypeSpinner.setItems(spinnerTypeValues)
        configureDatePicker(filterEstateOnMarketDate)

        val spinnerPhotoValues = listOf("0", "1", "2", "3", "4", "5+")
        filterPhotosNumber.setItems(spinnerPhotoValues)
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

    private fun configureDatePicker(datePickerEditText: EditText) {
        datePickerEditText.keyListener = null
        datePickerEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) onDatePickerClick()
        }
    }

    private fun onDatePickerClick() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this, R.style.DialogTheme,
            OnDateSetListener { datePicker: DatePicker?, _: Int, _: Int, _: Int ->
                val selectedTime = getTimeFromDatePicker(datePicker) ?: return@OnDateSetListener
                filterEstateOnMarketDate.setText(format.format(selectedTime).toString())
            },
            calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    private fun getTimeFromDatePicker(datePicker: DatePicker?): Date? {
        if (datePicker == null) return null

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, datePicker.year)
        calendar.set(Calendar.MONTH, datePicker.month)
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
        return calendar.time
    }
}
