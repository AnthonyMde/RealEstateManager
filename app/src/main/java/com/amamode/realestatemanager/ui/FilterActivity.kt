package com.amamode.realestatemanager.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.FilterEntity
import com.amamode.realestatemanager.domain.entity.InterestPoint
import com.amamode.realestatemanager.ui.creation.EstateType
import kotlinx.android.synthetic.main.activity_filter.*
import java.text.SimpleDateFormat
import java.util.*

const val FILTER_RESULT_CODE = 1026
const val FILTER_DATA_EXTRA = "FILTER_DATA"
const val FORMER_FILTER_DATA_EXTRA = "FORMER_FILTER_DATA"

class FilterActivity : AppCompatActivity() {
    private val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var selectedDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        setSupportActionBar(filterToolbar as Toolbar)
        title = getString(R.string.filter_toolbar_title)

        filterEstateCTA.setOnClickListener {
            val data = getFilterData()
            setResult(FILTER_RESULT_CODE, Intent().putExtra(FILTER_DATA_EXTRA, data))
            finish()
        }
        clearFilterDateCTA.setOnClickListener {
            if (selectedDate != null) {
                selectedDate = null
                filterEstateOnMarketDate.setText("")
                clearFilterDateCTA.visibility = INVISIBLE
            }
        }
        configureSpinnerAndDatePicker()
        val formerFilterData: FilterEntity? = intent.getParcelableExtra(FORMER_FILTER_DATA_EXTRA)
        formerFilterData?.let { updateData(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_filter -> {
                // use an empty FilterEntity to clear all data
                updateData(FilterEntity())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateData(data: FilterEntity) {
        filterEstateTypeSpinner.selectedIndex = getIndexEstateType(data.type ?: EstateType.UNKNOWN)
        filterEstateOwner.setText(data.owner ?: "")
        data.minPrice?.let { filterEstatePriceMin.setText(it.toString()) }
            ?: filterEstatePriceMin.setText("")
        data.maxPrice?.let { filterEstatePriceMax.setText(it.toString()) }
            ?: filterEstatePriceMax.setText("")
        data.minSurface?.let { filterEstateSurfaceMin.setText(it.toString()) }
            ?: filterEstateSurfaceMin.setText("")
        data.maxSurface?.let { filterEstateSurfaceMax.setText(it.toString()) }
            ?: filterEstateSurfaceMax.setText("")
        filterEstateCity.setText(data.city ?: "")
        data.zipCode?.let { filterEstateZipCode.setText(it.toString()) }
            ?: filterEstateZipCode.setText("")
        if (data.fromDate != null) {
            selectedDate = data.fromDate
            filterEstateOnMarketDate.setText(format.format(data.fromDate).toString())
            clearFilterDateCTA.visibility = VISIBLE
        } else {
            selectedDate = null
            filterEstateOnMarketDate.setText("")
            clearFilterDateCTA.visibility = INVISIBLE
        }
        filterPhotosNumber.selectedIndex = data.minPhotos ?: 0
        setInterestPoints(data.interestPoints ?: emptyList())
    }

    private fun configureSpinnerAndDatePicker() {
        val spinnerTypeValues = EstateType.values().map { getString(it.nameRes) }
            .toMutableList()
        spinnerTypeValues.add(0, getString(R.string.filter_type_all_estate))
        spinnerTypeValues.removeAt(spinnerTypeValues.size - 1)
        filterEstateTypeSpinner.setItems(spinnerTypeValues)
        configureDatePicker(filterEstateOnMarketDate)

        val spinnerPhotoValues = listOf("0", "1", "2", "3", "4+")
        filterPhotosNumber.setItems(spinnerPhotoValues)
    }

    private fun getFilterData(): FilterEntity {
        val ownerText = filterEstateOwner.text.toString().trim()
        val city = filterEstateCity.text.toString().trim()
        return FilterEntity(
            owner = if (ownerText.isNotEmpty()) ownerText else null,
            type = getFilterEstateType(filterEstateTypeSpinner.selectedIndex),
            minPrice = filterEstatePriceMin.text.toString().trim().toDoubleOrNull(),
            maxPrice = filterEstatePriceMax.text.toString().trim().toDoubleOrNull(),
            minSurface = filterEstateSurfaceMin.text.toString().trim().toIntOrNull(),
            maxSurface = filterEstateSurfaceMax.text.toString().trim().toIntOrNull(),
            fromDate = selectedDate,
            city = if (city.isNotEmpty()) city else null,
            zipCode = filterEstateZipCode.text.toString().trim().toIntOrNull(),
            minPhotos = filterPhotosNumber.selectedIndex,
            interestPoints = getInterestPoints()
        )
    }

    private fun getInterestPoints(): List<InterestPoint>? {
        val interestPoints = mutableListOf<InterestPoint>()
        if (filterInterestPointMetro.isChecked) interestPoints.add(InterestPoint.METRO)
        if (filterInterestPointShop.isChecked) interestPoints.add(InterestPoint.SHOP)
        if (filterInterestPointSchool.isChecked) interestPoints.add(InterestPoint.SCHOOL)
        if (filterInterestPointParc.isChecked) interestPoints.add(InterestPoint.PARC)

        return if (interestPoints.isNotEmpty()) interestPoints else null
    }

    private fun setInterestPoints(points: List<InterestPoint>) {
        filterInterestPointMetro.isChecked = points.contains(InterestPoint.METRO)
        filterInterestPointShop.isChecked = points.contains(InterestPoint.SHOP)
        filterInterestPointSchool.isChecked = points.contains(InterestPoint.SCHOOL)
        filterInterestPointParc.isChecked = points.contains(InterestPoint.PARC)
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

    private fun getIndexEstateType(type: EstateType): Int =
        when (type) {
            EstateType.HOUSE -> 1
            EstateType.APARTMENT -> 2
            EstateType.LOFT -> 3
            EstateType.DUPLEX -> 4
            EstateType.VILLA -> 5
            else -> 0
        }

    private fun configureDatePicker(datePickerEditText: EditText) {
        datePickerEditText.keyListener = null
        datePickerEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) onDatePickerClick()
        }
        datePickerEditText.setOnClickListener {
            onDatePickerClick()
        }
    }

    private fun onDatePickerClick() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this, R.style.DialogTheme,
            OnDateSetListener { datePicker: DatePicker?, _: Int, _: Int, _: Int ->
                val selectedTime = getTimeFromDatePicker(datePicker) ?: return@OnDateSetListener
                filterEstateOnMarketDate.setText(format.format(selectedTime).toString())
                clearFilterDateCTA.visibility = VISIBLE
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
        selectedDate = calendar.time
        return selectedDate
    }
}
