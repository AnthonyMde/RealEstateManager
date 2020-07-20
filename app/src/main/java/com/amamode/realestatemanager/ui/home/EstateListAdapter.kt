package com.amamode.realestatemanager.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.EstateDetails
import com.amamode.realestatemanager.domain.EstatePreview
import kotlinx.android.synthetic.main.item_estate_list.view.*

class EstateListAdapter(
    val onEstateClick: (Long, String) -> Unit
) :
    RecyclerView.Adapter<EstateListAdapter.ViewHolder>() {
    private var estateList: List<EstatePreview> = emptyList()

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

    fun setEstateList(estateDetailsList: List<EstatePreview>) {
        this.estateList = estateDetailsList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(estate: EstatePreview) {
            itemView.estateTitle.text = estate.type

            itemView.setOnClickListener {
                onEstateClick(estate.id, estate.type)
            }
        }
    }
}
