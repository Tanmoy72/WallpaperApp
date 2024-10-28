package com.paul.wallpaperapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.paul.wallpaperapp.R
import com.paul.wallpaperapp.model.CategoriesDataModel

class CategoriesAdapter(private val context: Context):RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private var categoriesList :ArrayList<CategoriesDataModel> = ArrayList()
    fun setCategoriesList(newList: ArrayList<CategoriesDataModel>) {
         categoriesList= newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.categories_item, parent, false)
        return CategoriesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val currentItem = categoriesList[position]
        holder.categoriesItemText.text = currentItem.title
        holder.categoriesItemImage.setImageResource(currentItem.image)
    }
    class CategoriesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val categoriesItemImage :ImageView = itemView.findViewById(R.id.categoriesItemImage)
        val categoriesItemText :TextView =itemView.findViewById(R.id.categoriesItemText)
    }
}