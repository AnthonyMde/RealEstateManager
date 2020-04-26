package com.amamode.realestatemanager.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.Estate
import kotlinx.android.synthetic.main.item_estate_list.view.*

class EstateListAdapter(
    val onEstateClick: (Estate) -> Unit
) :
    RecyclerView.Adapter<EstateListAdapter.ViewHolder>() {
    private var estateList: List<Estate> = emptyList()

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

    fun setEstateList(estateList: List<Estate>) {
        this.estateList = estateList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(estate: Estate) {
            itemView.estateTitle.text = estate.owner

            itemView.setOnClickListener {
                onEstateClick(estate)
            }
        }
    }
}
