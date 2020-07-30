package com.amamode.realestatemanager.ui.creation

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.item_creation_photo.view.*
import java.io.File

class EstateCreationPhotoAdapter() : RecyclerView.Adapter<EstateCreationPhotoAdapter.ViewHolder>() {
    private var estatePhotoUrlList: List<File> = emptyList()

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

    fun setPhotoUrlList(estatePhotoList: List<File>) {
        this.estatePhotoUrlList = estatePhotoList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(file: File) {
            val uri = Uri.fromFile(file)
            itemView.estatePhoto.setImageURI(uri)
        }
    }
}
