package com.mobdeve.s12.fallarme.sophia.bookbuddy.collection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.fallarme.sophia.bookbuddy.R

class CurrentlyReadingAdapter(private val data: ArrayList<Book>) :
    RecyclerView.Adapter<CurrentlyReadingAdapter.SampleViewHolder>() {

    class SampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookTitle: TextView = itemView.findViewById(R.id.tvwBookTitle)
        val bookAuthor: TextView = itemView.findViewById(R.id.tvwAuthor)
        val bookCover: ImageView = itemView.findViewById(R.id.imgvwBookCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.currentlyreading_layout, parent, false)
        return SampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
        val book = data[position]
        holder.bookTitle.text = book.title
        holder.bookAuthor.text = book.author
        holder.bookCover.setImageResource(book.coverResId)
    }

    override fun getItemCount() = data.size
}