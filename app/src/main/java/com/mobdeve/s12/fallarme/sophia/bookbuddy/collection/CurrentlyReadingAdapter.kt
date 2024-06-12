package com.mobdeve.s12.fallarme.sophia.bookbuddy.collection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R

class CurrentlyReadingAdapter(private val data: List<String>) :
    RecyclerView.Adapter<CurrentlyReadingAdapter.SampleViewHolder>() {

    class SampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tvwBook)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.currentlyreading_layout, parent, false)
        return SampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
        holder.textView.text = data[position]
    }

    override fun getItemCount() = data.size
}