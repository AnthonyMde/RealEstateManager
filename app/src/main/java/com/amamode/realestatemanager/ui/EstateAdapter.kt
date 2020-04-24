package com.amamode.realestatemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.data.EstateEntity
import kotlinx.android.synthetic.main.item_estate_list.view.*

class EstateAdapter(
    val onEstateClick: (EstateEntity) -> Unit
) :
    RecyclerView.Adapter<EstateAdapter.ViewHolder>() {
    private var estateList: List<EstateEntity> = listOf(
        EstateEntity(1, "toto"),
        EstateEntity(2, "tota"),
        EstateEntity(3, "tote"),
        EstateEntity(4, "toti")
    )

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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(estate: EstateEntity) {
            itemView.estateTitle.text = estate.name

            itemView.setOnClickListener {
                onEstateClick(estate)
            }
        }
    }
}
