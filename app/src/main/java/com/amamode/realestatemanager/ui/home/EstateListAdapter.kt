package com.amamode.realestatemanager.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.EstateDetails
import com.amamode.realestatemanager.ui.creation.EstateType
import kotlinx.android.synthetic.main.item_estate_list.view.*
import org.jetbrains.anko.image

class EstateListAdapter(
    val onEstateClick: (Long, EstateType) -> Unit
) :
    RecyclerView.Adapter<EstateListAdapter.ViewHolder>() {
    private var estateList: List<EstateDetails> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_estate_list,
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

            itemView.estateItemPrice.text = "$" + estate.price

            itemView.estateItemPic.setImageURI(estate.estatePhotos.get(0).first)

            itemView.setOnClickListener {
                onEstateClick(estate.id, estate.type)
            }
        }
    }
}
