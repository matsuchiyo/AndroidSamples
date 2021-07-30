package com.matsuchiyo.customviews

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

enum class MenuItemType(val title: String) {
    CIRCLE_PROGRESS_VIEW("Circle progress view")
}

class MenuItemAdapter: RecyclerView.Adapter<MenuItemViewHolder>() {

    var onItemClickListener: ((MenuItemType) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MenuItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val menuItemType = MenuItemType.values()[position]
        holder.titleText.text = menuItemType.title
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(menuItemType)
        }
    }

    override fun getItemCount(): Int {
        Log.d("AAA", "*** MenuItemType.values().count(): ${MenuItemType.values().count()}")
        return MenuItemType.values().count()
    }
}

class MenuItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleText: TextView = itemView.findViewById(R.id.title_text)
}