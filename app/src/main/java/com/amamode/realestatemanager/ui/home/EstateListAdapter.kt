package com.amamode.realestatemanager.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.CurrencyType
import com.amamode.realestatemanager.domain.entity.EstateDetails
import com.amamode.realestatemanager.ui.CurrencyViewModel
import com.amamode.realestatemanager.ui.creation.EstateType
import kotlinx.android.synthetic.main.item_estate_list.view.*

class EstateListAdapter(
    val onEstateClick: (Long, EstateType) -> Unit,
    val context: Context?,
    var currentCurrency: CurrencyType
) :
    RecyclerView.Adapter<EstateListAdapter.ViewHolder>() {
    private var estateList: List<EstateDetails> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val isTablet = context?.resources?.getBoolean(R.bool.isTablet)
        val view = if (isTablet == true) {
            R.layout.item_estate_list_tablet
        } else {
            R.layout.item_estate_list
        }
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                view,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return estateList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(estateList[position])
    }

    fun setEstateList(estateDetailsList: List<EstateDetails>) {
        this.estateList = estateDetailsList
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(estate: EstateDetails) {
            itemView.estateItemType.text = itemView.context.getString(estate.type.nameRes)

            val cityName = estate.address?.city
            if (cityName == null || cityName == "") {
                itemView.estateItemCity.visibility = View.GONE
            } else {
                itemView.estateItemCity.visibility = View.VISIBLE
                itemView.estateItemCity.text = cityName
            }

            itemView.estateItemPrice.text = when (currentCurrency) {
                CurrencyType.EURO -> "${estate.price} €"
                CurrencyType.DOLLAR -> {
                    CurrencyViewModel.getDollarPriceString(context, estate.price)
                }
            }

            itemView.estateSoldStamp.visibility = if (estate.status.sold) VISIBLE else INVISIBLE

            itemView.estateItemPic.setImageURI(estate.estatePhotos[0].first)

            itemView.setOnClickListener {
                onEstateClick(estate.id, estate.type)
            }
        }
    }
}
