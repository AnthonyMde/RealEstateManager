package com.amamode.realestatemanager.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.EstateDetails
import com.amamode.realestatemanager.ui.creation.EstateType
import kotlinx.android.synthetic.main.item_estate_list.view.*

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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(estate: EstateDetails) {
            itemView.estateTitle.text = itemView.context.getString(estate.type.nameRes)

            itemView.setOnClickListener {
                onEstateClick(estate.id, estate.type)
            }
        }
    }
}
