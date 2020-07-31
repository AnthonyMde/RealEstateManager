package com.amamode.realestatemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.item_creation_photo.view.*

class EstatePhotoAdapter() : RecyclerView.Adapter<EstatePhotoAdapter.ViewHolder>() {
    // first is photo uri, second is photo description
    private var estatePhotoList: List<Pair<String, String>> = emptyList()

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
        return estatePhotoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(estatePhotoList[position])
    }

    fun setPhotoUrlList(estatePhotosList: List<Pair<String, String>>) {
        this.estatePhotoList = estatePhotosList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(photo: Pair<String, String>) {
            itemView.estatePhoto.setImageURI(photo.first)
            itemView.estataPhotoDescription.text = photo.second
        }
    }
}
