package com.paul.wallpaperapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.paul.wallpaperapp.R


import com.paul.wallpaperapp.model.WallPaperDataModel

class WallPaperAdapter(private val context: Context,
                       private val onItemClick: (String) -> Unit)
    :RecyclerView.Adapter<WallPaperAdapter.WallPaperViewHolder>() {


    private var wallPaperList:ArrayList<WallPaperDataModel> = ArrayList()
    fun setWallPaperList(newList: ArrayList<WallPaperDataModel>) {
        wallPaperList= newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallPaperViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.wallpaper_item, parent, false)
        return WallPaperViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return wallPaperList.size
    }

    override fun onBindViewHolder(holder: WallPaperViewHolder, position: Int) {
        val currentItem = wallPaperList[position]
        Glide.with(context)
            .load(currentItem.large)
            .into(holder.wallPaperImageItem)
        //Log.d("@@@JAPAN","${currentItem.large}")
        // Set click listener here
        holder.itemView.setOnClickListener {
            onItemClick(currentItem.large) // Pass image URL to lambda
        }



    }
    class WallPaperViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val wallPaperImageItem : ImageView = itemView.findViewById(R.id.wallPaperImageItem)


    }
}

