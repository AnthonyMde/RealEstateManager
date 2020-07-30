package com.amamode.realestatemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.item_creation_photo.view.*

class EstatePhotoAdapter() : RecyclerView.Adapter<EstatePhotoAdapter.ViewHolder>() {
    private var estatePhotoUrlList: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_creation_photo,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return estatePhotoUrlList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(estatePhotoUrlList[position])
    }

    fun setPhotoUrlList(estatePhotoUrlList: List<String>) {
        this.estatePhotoUrlList = estatePhotoUrlList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(url: String) {
            itemView.estatePhoto.setImageURI(url)
        }
    }
}
