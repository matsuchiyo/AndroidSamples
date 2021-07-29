package com.matsuchiyo.infiniterecyclerviewsample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter: RecyclerView.Adapter<ItemViewHolder>() {

    companion object {
        const val LOOP_COUNT_FOR_INFINITE_SCROLL = 100
    }

    private var onItemClickListener: ((Item, View) -> Unit)? = null

    var items: List<Item> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val realPosition = position % items.count()
        val item = items[realPosition]
        holder.textView.text = item.name
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item, it)
        }
    }

    override fun getItemCount(): Int {
        return items.count() * LOOP_COUNT_FOR_INFINITE_SCROLL
    }

    fun getRealItemCount(): Int {
        return items.count()
    }

    fun setOnItemClickListener(onItemClickListener: (Item, View) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }
}

class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.name_text)
}